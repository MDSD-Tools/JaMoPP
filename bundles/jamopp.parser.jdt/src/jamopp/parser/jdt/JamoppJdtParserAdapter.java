package jamopp.parser.jdt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;

import jamopp.parser.api.JaMoPPParserAPI;

class JamoppJdtParserAdapter implements JaMoPPParserAPI {

	public final String encoding;
	public final String javaVersion;

	private final ContainersFactory containersFactory;
	private final UtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private final UtilJdtResolver jdtResolverUtility;
	private final VisitorAndConverterOrdinaryCompilationUnitJDTAST converter;

	private final JamoppClasspathEntriesSearcher jamoppClasspathEntriesSearcher;
	private final JamoppJavaParserFactory jamoppJavaParserFactory;
	private final JamoppCompilationUnitsFactory jamoppCompilationUnitsFactory;
	private final JamoppFileWithJDTParser jamoppFileWithJDTParser;

	private final Logger logger = Logger.getLogger(JaMoPPJDTParser.class.getSimpleName());

	String[] getClasspathEntries(Path dir) {
		return jamoppClasspathEntriesSearcher.getClasspathEntries(dir);
	}

	Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings) {
		return jamoppCompilationUnitsFactory.getCompilationUnits(parser, classpathEntries, sources, encodings);
	}

	ASTParser getJavaParser(String version) {
		return jamoppJavaParserFactory.getJavaParser(version);
	}

	private ResourceSet resourceSet;

	JamoppJdtParserAdapter(String javaVersion, String encoding) {

		this.encoding = encoding;
		this.javaVersion = javaVersion;
		typeInstructionSeparationUtility = InjectorMine.getTypeInstructionSeparationUtility();
		jdtResolverUtility = InjectorMine.getJDTResolverUtility();
		converter = InjectorMine.getOrdinaryCompilationUnitJDTASTVisitorAndConverter();
		containersFactory = InjectorMine.getContainersFactory();

		jamoppClasspathEntriesSearcher = new JamoppClasspathEntriesSearcher(logger);
		jamoppJavaParserFactory = new JamoppJavaParserFactory(logger);
		jamoppCompilationUnitsFactory = new JamoppCompilationUnitsFactory(logger);
		jamoppFileWithJDTParser = new JamoppFileWithJDTParser(jamoppJavaParserFactory, javaVersion);

		containersFactory.createEmptyModel();
		this.resourceSet = new ResourceSetImpl();
		jdtResolverUtility.setResourceSet(this.resourceSet);
	}

	List<JavaRoot> convertCompilationUnits(Map<String, CompilationUnit> compilationUnits) {
		final List<JavaRoot> result = new ArrayList<>();
		for (final String sourceFilePath : compilationUnits.keySet()) {
			compilationUnits.get(sourceFilePath).accept(converter);
			final JavaRoot root = converter.getConvertedElement();
			Resource newResource;
			if (root.eResource() == null) {
				newResource = JamoppJdtParserAdapter.this.resourceSet.createResource(URI.createFileURI(sourceFilePath));
				newResource.getContents().add(root);
			} else {
				newResource = root.eResource();
				if (!newResource.getURI().toFileString().equals(sourceFilePath)) {
					newResource.setURI(URI.createFileURI(sourceFilePath));
				}
			}
			result.add(root);
		}

		typeInstructionSeparationUtility.convertAll();
		jdtResolverUtility.completeResolution();
		for (final Resource res : new ArrayList<>(this.resourceSet.getResources())) {
			if (res.getContents().isEmpty()) {
				try {
					res.delete(this.resourceSet.getLoadOptions());
				} catch (final IOException e) {
					logger.error(res.getURI(), e);
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final JamoppJdtParserAdapter other = (JamoppJdtParserAdapter) obj;
		return Objects.equals(this.resourceSet, other.resourceSet);
	}

	<T> Set<T> get(Class<T> type) {
		return resourceSet.getResources().stream().filter(Objects::nonNull)
				.filter(r -> (!r.getContents().isEmpty() && !"file".equals(r.getURI().scheme())))
				.map(r -> r.getContents().get(0)).filter(Objects::nonNull).filter(type::isInstance).map(type::cast)
				.collect(Collectors.toSet());
	}

	/**
	 * @return the resourceSet
	 */
	ResourceSet getResourceSet() {
		return resourceSet;
	}

	String[] getSourcepathEntries(Path dir) {
		try (Stream<Path> paths = Files.walk(dir)) {
			return paths
					.filter(path -> Files.isRegularFile(path)
							&& path.getFileName().toString().toLowerCase().endsWith("java"))
					.map(Path::toAbsolutePath).map(Path::normalize).map(Path::toString).filter(p -> {
						final Resource r = JavaClasspath.get().getResource(URI.createFileURI(p));
						if (r != null) {
							JamoppJdtParserAdapter.this.resourceSet.getResources().add(r);
							return false;
						}
						return true;
					}).toArray(i -> new String[i]);
		} catch (final IOException e) {
			logger.error(dir, e);
			return new String[0];
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.resourceSet);
	}

	@Override
	public JavaRoot parse(String fileName, InputStream input) {
		final Resource r = JavaClasspath.get().getResource(URI.createFileURI(fileName));
		if (r != null) {
			return (JavaRoot) r.getContents().get(0);
		}
		final StringBuilder builder = new StringBuilder();
		final String lineSep = System.lineSeparator();
		try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(input))) {
			buffReader.lines().forEach(line -> builder.append(line + lineSep));
		} catch (final IOException e) {
			logger.error(input, e);
		}
		final String src = builder.toString();
		final ASTNode ast = jamoppFileWithJDTParser.parseFileWithJDT(src, fileName);
		converter.setSource(src);
		ast.accept(converter);
		typeInstructionSeparationUtility.convertAll();
		jdtResolverUtility.completeResolution();
		this.resourceSet = null;
		return converter.getConvertedElement();
	}

	ResourceSet parseDirectory(ASTParser parser, Path dir) {
		final String[] sources = getSourcepathEntries(dir);
		final String[] encodings = new String[sources.length];
		Arrays.fill(encodings, encoding);
		this.parseFilesWithJDT(parser, getClasspathEntries(dir), sources, encodings);

		final ResourceSet result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	@Override
	public ResourceSet parseDirectory(Path dir) {
		return parseDirectory(getJavaParser(javaVersion), dir);
	}

	@Override
	public Resource parseFile(Path file) {
		Resource result = JavaClasspath.get().getResource(URI.createFileURI(file.toAbsolutePath().toString()));
		if (result != null) {
			return result;
		}
		result = this
				.parseFilesWithJDT(getJavaParser(javaVersion), new String[] {},
						new String[] { file.toAbsolutePath().toString() }, new String[] { encoding })
				.get(0).eResource();
		this.resourceSet = null;
		return result;
	}

	private List<JavaRoot> parseFilesWithJDT(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings) {
		final Map<String, CompilationUnit> compilationUnits = getCompilationUnits(parser, classpathEntries, sources,
				encodings);
		return convertCompilationUnits(compilationUnits);
	}

	ResourceSet parsePackage(IPackageFragment javaPackage) {
		final Map<String, CompilationUnit> compilationUnits = new HashMap<>();

		try {
			for (final ICompilationUnit unit : javaPackage.getCompilationUnits()) {
				if (unit instanceof CompilationUnit) {
					compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
				}

			}
		} catch (final JavaModelException e) {
			logger.error(javaPackage, e);
		}

		this.convertCompilationUnits(compilationUnits);
		final ResourceSet result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	ResourceSet parseProject(IJavaProject javaProject) {
		final Map<String, CompilationUnit> compilationUnits = new HashMap<>();

		try {
			for (final IPackageFragment mypackage : javaProject.getPackageFragments()) {
				for (final ICompilationUnit unit : mypackage.getCompilationUnits()) {
					if (unit instanceof CompilationUnit) {
						compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
					}

				}
			}
		} catch (final JavaModelException e) {
			logger.error(javaProject, e);
		}

		this.convertCompilationUnits(compilationUnits);
		final ResourceSet result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		this.resourceSet = Objects.requireNonNull(set);
	}

	@Override
	public String toString() {
		return "JaMoPPJDTParser [resourceSet=" + resourceSet + "]";
	}

	ASTNode parseFileWithJDT(String src, String fileName) {
		return jamoppFileWithJDTParser.parseFileWithJDT(src, fileName);
	}

}
