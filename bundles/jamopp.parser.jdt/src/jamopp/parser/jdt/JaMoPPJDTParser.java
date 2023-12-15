package jamopp.parser.jdt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;

import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.name.Names;

import jamopp.parser.api.JaMoPPParserAPI;
import jamopp.parser.jdt.injection.ConverterModule;
import jamopp.parser.jdt.injection.FactoryModule;
import jamopp.parser.jdt.injection.HandlerModule;
import jamopp.parser.jdt.injection.JamoppModule;
import jamopp.parser.jdt.injection.UtilModule;
import jamopp.parser.jdt.injection.VisitorModule;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.interfaces.jamopp.JamoppClasspathEntriesSearcher;
import jamopp.parser.jdt.interfaces.jamopp.JamoppCompilationUnitsFactory;
import jamopp.parser.jdt.interfaces.jamopp.JamoppFileWithJDTParser;
import jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;
import jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public final class JaMoPPJDTParser implements JaMoPPParserAPI {

	private static final ContainersFactory containersFactory;
	public static final String DEFAULT_ENCODING;

	public static final String DEFAULT_JAVA_VERSION;
	private static final JamoppClasspathEntriesSearcher jamoppClasspathEntriesSearcher;
	private static final JamoppCompilationUnitsFactory jamoppCompilationUnitsFactory;
	private static final JamoppFileWithJDTParser jamoppFileWithJDTParser;
	private static final JamoppJavaParserFactory jamoppJavaParserFactory;
	private static final UtilJdtResolver jdtResolverUtility;
	private static final Logger logger;
	private static final UtilTypeInstructionSeparation typeInstructionSeparationUtility;
	private static final AbstractVisitor visitor;

	static {
		var injector = Guice.createInjector(new UtilModule(), new FactoryModule(), new ConverterModule(),
				new HandlerModule(), new VisitorModule(), new JamoppModule());

		DEFAULT_ENCODING = injector.getInstance(Key.get(String.class, Names.named("DEFAULT_ENCODING")));
		DEFAULT_JAVA_VERSION = injector.getInstance(Key.get(String.class, Names.named("DEFAULT_JAVA_VERSION")));
		visitor = injector.getInstance(AbstractVisitor.class);
		jdtResolverUtility = injector.getInstance(UtilJdtResolver.class);
		typeInstructionSeparationUtility = injector.getInstance(UtilTypeInstructionSeparation.class);
		containersFactory = injector.getInstance(ContainersFactory.class);
		logger = injector.getInstance(Logger.class);
		jamoppClasspathEntriesSearcher = injector.getInstance(JamoppClasspathEntriesSearcher.class);
		jamoppCompilationUnitsFactory = injector.getInstance(JamoppCompilationUnitsFactory.class);
		jamoppJavaParserFactory = injector.getInstance(JamoppJavaParserFactory.class);
		jamoppFileWithJDTParser = injector.getInstance(JamoppFileWithJDTParser.class);
	}

	public static String[] getClasspathEntries(Path dir) {
		return jamoppClasspathEntriesSearcher.getClasspathEntries(dir);
	}

	public static Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries,
			String[] sources, String[] encodings) {
		return jamoppCompilationUnitsFactory.getCompilationUnits(parser, classpathEntries, sources, encodings);
	}

	public static ASTParser getJavaParser(String version) {
		return jamoppJavaParserFactory.getJavaParser(version);
	}

	private ResourceSet resourceSet;

	public JaMoPPJDTParser() {
		containersFactory.createEmptyModel();
		this.resourceSet = new ResourceSetImpl();
		jdtResolverUtility.setResourceSet(this.resourceSet);
	}

	public List<JavaRoot> convertCompilationUnits(Map<String, CompilationUnit> compilationUnits) {
		final List<JavaRoot> result = new ArrayList<>();
		for (final String sourceFilePath : compilationUnits.keySet()) {
			compilationUnits.get(sourceFilePath).accept(visitor);
			final var root = visitor.getConvertedElement();
			Resource newResource;
			if (root.eResource() == null) {
				newResource = JaMoPPJDTParser.this.resourceSet.createResource(URI.createFileURI(sourceFilePath));
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
		final var other = (JaMoPPJDTParser) obj;
		return Objects.equals(this.resourceSet, other.resourceSet);
	}

	public <T> Set<T> get(Class<T> type) {
		return this.resourceSet.getResources().stream().filter(Objects::nonNull)
				.filter(r -> (!r.getContents().isEmpty() && !"file".equals(r.getURI().scheme())))
				.map(r -> r.getContents().get(0)).filter(Objects::nonNull).filter(type::isInstance).map(type::cast)
				.collect(Collectors.toSet());
	}

	public ResourceSet getResourceSet() {
		return this.resourceSet;
	}

	public String[] getSourcepathEntries(Path dir) {
		try (var paths = Files.walk(dir)) {
			return paths
					.filter(path -> Files.isRegularFile(path)
							&& path.getFileName().toString().toLowerCase().endsWith("java"))
					.map(Path::toAbsolutePath).map(Path::normalize).map(Path::toString).filter(p -> {
						final var r = JavaClasspath.get().getResource(URI.createFileURI(p));
						if (r != null) {
							JaMoPPJDTParser.this.resourceSet.getResources().add(r);
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
		final var r = JavaClasspath.get().getResource(URI.createFileURI(fileName));
		if (r != null) {
			return (JavaRoot) r.getContents().get(0);
		}
		final var builder = new StringBuilder();
		final var lineSep = System.lineSeparator();
		try (var buffReader = new BufferedReader(new InputStreamReader(input))) {
			buffReader.lines().forEach(line -> builder.append(line + lineSep));
		} catch (final IOException e) {
			logger.error(input, e);
		}
		final var src = builder.toString();
		final var ast = jamoppFileWithJDTParser.parseFileWithJDT(src, fileName);
		visitor.setSource(src);
		ast.accept(visitor);
		typeInstructionSeparationUtility.convertAll();
		jdtResolverUtility.completeResolution();
		this.resourceSet = null;
		return visitor.getConvertedElement();
	}

	public ResourceSet parseDirectory(ASTParser parser, Path dir) {
		final var sources = getSourcepathEntries(dir);
		final var encodings = new String[sources.length];
		Arrays.fill(encodings, DEFAULT_ENCODING);
		this.parseFilesWithJDT(parser, getClasspathEntries(dir), sources, encodings);

		final var result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	@Override
	public ResourceSet parseDirectory(Path dir) {
		return parseDirectory(getJavaParser(DEFAULT_JAVA_VERSION), dir);
	}

	@Override
	public Resource parseFile(Path file) {
		var result = JavaClasspath.get().getResource(URI.createFileURI(file.toAbsolutePath().toString()));
		if (result != null) {
			return result;
		}
		result = this
				.parseFilesWithJDT(getJavaParser(DEFAULT_JAVA_VERSION), new String[] {},
						new String[] { file.toAbsolutePath().toString() }, new String[] { DEFAULT_ENCODING })
				.get(0).eResource();
		this.resourceSet = null;
		return result;
	}

	private List<JavaRoot> parseFilesWithJDT(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings) {
		final var compilationUnits = getCompilationUnits(parser, classpathEntries, sources, encodings);
		return convertCompilationUnits(compilationUnits);
	}

	public ResourceSet parsePackage(IPackageFragment javaPackage) {
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
		final var result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	public ResourceSet parseProject(IJavaProject javaProject) {
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
		final var result = this.resourceSet;
		this.resourceSet = null;
		return result;
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		this.resourceSet = Objects.requireNonNull(set);
	}

	@Override
	public String toString() {
		return "JaMoPPJDTParser [resourceSet=" + this.resourceSet + "]";
	}

}