package tools.mdsd.jamopp.parser.jdt;

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
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.parser.api.JaMoPPParserAPI;
import tools.mdsd.jamopp.parser.jdt.injection.ConverterModule;
import tools.mdsd.jamopp.parser.jdt.injection.FactoryModule;
import tools.mdsd.jamopp.parser.jdt.injection.HelperModule;
import tools.mdsd.jamopp.parser.jdt.injection.JamoppModule;
import tools.mdsd.jamopp.parser.jdt.injection.ResolverModule;
import tools.mdsd.jamopp.parser.jdt.injection.VisitorModule;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.jdt.interfaces.jamopp.JamoppClasspathEntriesSearcher;
import tools.mdsd.jamopp.parser.jdt.interfaces.jamopp.JamoppCompilationUnitsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.jamopp.JamoppFileWithJDTParser;
import tools.mdsd.jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class JaMoPPJDTParser implements JaMoPPParserAPI {

	public final static String DEFAULT_ENCODING;
	public final static String DEFAULT_JAVA_VERSION;
	private final static Logger LOGGER;
	private final static AbstractVisitor VISITOR;
	private final static ContainersFactory CONTAINERS_FACTORY;
	private final static JamoppClasspathEntriesSearcher JAMOPP_CLASSPATH_ENTRIES_SEARCHER;
	private final static JamoppCompilationUnitsFactory JAMOPP_COMPILATION_UNITS_FACTORY;
	private final static JamoppFileWithJDTParser JAMOPP_FILE_WITH_JDT_PARSER;
	private final static JamoppJavaParserFactory JAMOPP_JAVA_PARSER_FACTORY;
	private final static JdtResolver UTIL_JDT_RESOLVER;
	private final static UtilTypeInstructionSeparation UTIL_TYPE_INSTRUCTION_SEPARATION;

	static {
		Injector injector = Guice.createInjector(new HelperModule(), new FactoryModule(), new ConverterModule(),
				new VisitorModule(), new JamoppModule(), new ResolverModule());

		DEFAULT_ENCODING = injector.getInstance(Key.get(String.class, Names.named("DEFAULT_ENCODING")));
		DEFAULT_JAVA_VERSION = injector.getInstance(Key.get(String.class, Names.named("DEFAULT_JAVA_VERSION")));
		VISITOR = injector.getInstance(AbstractVisitor.class);
		UTIL_JDT_RESOLVER = injector.getInstance(JdtResolver.class);
		UTIL_TYPE_INSTRUCTION_SEPARATION = injector.getInstance(UtilTypeInstructionSeparation.class);
		CONTAINERS_FACTORY = injector.getInstance(ContainersFactory.class);
		LOGGER = injector.getInstance(Logger.class);
		JAMOPP_CLASSPATH_ENTRIES_SEARCHER = injector.getInstance(JamoppClasspathEntriesSearcher.class);
		JAMOPP_COMPILATION_UNITS_FACTORY = injector.getInstance(JamoppCompilationUnitsFactory.class);
		JAMOPP_JAVA_PARSER_FACTORY = injector.getInstance(JamoppJavaParserFactory.class);
		JAMOPP_FILE_WITH_JDT_PARSER = injector.getInstance(JamoppFileWithJDTParser.class);
	}

	public static String[] getClasspathEntries(Path dir) {
		return JAMOPP_CLASSPATH_ENTRIES_SEARCHER.getClasspathEntries(dir);
	}

	public static Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries,
			String[] sources, String[] encodings) {
		return JAMOPP_COMPILATION_UNITS_FACTORY.getCompilationUnits(parser, classpathEntries, sources, encodings);
	}

	public static ASTParser getJavaParser(String version) {
		return JAMOPP_JAVA_PARSER_FACTORY.getJavaParser(version);
	}

	private ResourceSet resourceSet;

	public JaMoPPJDTParser() {
		CONTAINERS_FACTORY.createEmptyModel();
		resourceSet = new ResourceSetImpl();
		UTIL_JDT_RESOLVER.setResourceSet(resourceSet);
	}

	public List<JavaRoot> convertCompilationUnits(Map<String, CompilationUnit> compilationUnits) {
		List<JavaRoot> result = new ArrayList<>();
		for (String sourceFilePath : compilationUnits.keySet()) {
			compilationUnits.get(sourceFilePath).accept(VISITOR);
			JavaRoot root = VISITOR.getConvertedElement();
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

		UTIL_TYPE_INSTRUCTION_SEPARATION.convertAll();
		UTIL_JDT_RESOLVER.completeResolution();
		for (Resource res : new ArrayList<>(resourceSet.getResources())) {
			if (res.getContents().isEmpty()) {
				try {
					res.delete(resourceSet.getLoadOptions());
				} catch (IOException e) {
					LOGGER.error(res.getURI(), e);
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
		JaMoPPJDTParser other = (JaMoPPJDTParser) obj;
		return Objects.equals(resourceSet, other.resourceSet);
	}

	public <T> Set<T> get(Class<T> type) {
		return resourceSet.getResources().stream().filter(Objects::nonNull)
				.filter(r -> (!r.getContents().isEmpty() && !"file".equals(r.getURI().scheme())))
				.map(r -> r.getContents().get(0)).filter(Objects::nonNull).filter(type::isInstance).map(type::cast)
				.collect(Collectors.toSet());
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public String[] getSourcepathEntries(Path dir) {
		try (Stream<Path> paths = Files.walk(dir)) {
			return paths
					.filter(path -> Files.isRegularFile(path)
							&& path.getFileName().toString().toLowerCase().endsWith("java"))
					.map(Path::toAbsolutePath).map(Path::normalize).map(Path::toString).filter(p -> {
						Resource r = JavaClasspath.get().getResource(URI.createFileURI(p));
						if (r != null) {
							JaMoPPJDTParser.this.resourceSet.getResources().add(r);
							return false;
						}
						return true;
					}).toArray(i -> new String[i]);
		} catch (IOException e) {
			LOGGER.error(dir, e);
			return new String[0];
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceSet);
	}

	@Override
	public JavaRoot parse(String fileName, InputStream input) {
		Resource r = JavaClasspath.get().getResource(URI.createFileURI(fileName));
		if (r != null) {
			return (JavaRoot) r.getContents().get(0);
		}
		StringBuilder builder = new StringBuilder();
		String lineSep = System.lineSeparator();
		try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(input))) {
			buffReader.lines().forEach(line -> builder.append(line + lineSep));
		} catch (IOException e) {
			LOGGER.error(input, e);
		}
		String src = builder.toString();
		ASTNode ast = JAMOPP_FILE_WITH_JDT_PARSER.parseFileWithJDT(src, fileName);
		VISITOR.setSource(src);
		ast.accept(VISITOR);
		UTIL_TYPE_INSTRUCTION_SEPARATION.convertAll();
		UTIL_JDT_RESOLVER.completeResolution();
		resourceSet = null;
		return VISITOR.getConvertedElement();
	}

	public ResourceSet parseDirectory(ASTParser parser, Path dir) {
		String[] sources = getSourcepathEntries(dir);
		String[] encodings = new String[sources.length];
		Arrays.fill(encodings, DEFAULT_ENCODING);
		parseFilesWithJDT(parser, getClasspathEntries(dir), sources, encodings);

		ResourceSet result = resourceSet;
		resourceSet = null;
		return result;
	}

	@Override
	public ResourceSet parseDirectory(Path dir) {
		return parseDirectory(getJavaParser(DEFAULT_JAVA_VERSION), dir);
	}

	@Override
	public Resource parseFile(Path file) {
		Resource result = JavaClasspath.get().getResource(URI.createFileURI(file.toAbsolutePath().toString()));
		if (result != null) {
			return result;
		}
		result = parseFilesWithJDT(getJavaParser(DEFAULT_JAVA_VERSION), new String[] {},
				new String[] { file.toAbsolutePath().toString() }, new String[] { DEFAULT_ENCODING }).get(0)
				.eResource();
		resourceSet = null;
		return result;
	}

	private List<JavaRoot> parseFilesWithJDT(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings) {
		Map<String, CompilationUnit> compilationUnits = getCompilationUnits(parser, classpathEntries, sources,
				encodings);
		return convertCompilationUnits(compilationUnits);
	}

	public ResourceSet parsePackage(IPackageFragment javaPackage) {
		Map<String, CompilationUnit> compilationUnits = new HashMap<>();

		try {
			for (ICompilationUnit unit : javaPackage.getCompilationUnits()) {
				if (unit instanceof CompilationUnit) {
					compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
				}

			}
		} catch (JavaModelException e) {
			LOGGER.error(javaPackage, e);
		}

		convertCompilationUnits(compilationUnits);
		ResourceSet result = resourceSet;
		resourceSet = null;
		return result;
	}

	public ResourceSet parseProject(IJavaProject javaProject) {
		Map<String, CompilationUnit> compilationUnits = new HashMap<>();

		try {
			for (IPackageFragment mypackage : javaProject.getPackageFragments()) {
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					if (unit instanceof CompilationUnit) {
						compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
					}

				}
			}
		} catch (JavaModelException e) {
			LOGGER.error(javaProject, e);
		}

		convertCompilationUnits(compilationUnits);
		ResourceSet result = resourceSet;
		resourceSet = null;
		return result;
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		resourceSet = Objects.requireNonNull(set);
	}

	@Override
	public String toString() {
		return "JaMoPPJDTParser [resourceSet=" + resourceSet + "]";
	}

}