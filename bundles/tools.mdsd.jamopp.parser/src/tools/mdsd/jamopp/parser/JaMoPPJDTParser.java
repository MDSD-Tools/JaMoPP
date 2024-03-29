package tools.mdsd.jamopp.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
import tools.mdsd.jamopp.parser.injection.ConverterModule;
import tools.mdsd.jamopp.parser.injection.FactoryModule;
import tools.mdsd.jamopp.parser.injection.HelperModule;
import tools.mdsd.jamopp.parser.injection.JamoppModule;
import tools.mdsd.jamopp.parser.injection.ResolverModule;
import tools.mdsd.jamopp.parser.injection.VisitorModule;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppClasspathEntriesSearcher;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppCompilationUnitsFactory;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppFileWithJDTParser;
import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppJavaParserFactory;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.interfaces.visitor.AbstractVisitor;

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

	private ResourceSet resourceSet;

	static {
		final Injector injector = Guice.createInjector(new HelperModule(), new FactoryModule(), new ConverterModule(),
				new VisitorModule(), new JamoppModule(JaMoPPJDTParser.class.getSimpleName()), new ResolverModule());

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

	public JaMoPPJDTParser() {
		CONTAINERS_FACTORY.createEmptyModel();
		resourceSet = new ResourceSetImpl();
		UTIL_JDT_RESOLVER.setResourceSet(resourceSet);
	}

	@Override
	public List<JavaRoot> convertCompilationUnits(final Map<String, CompilationUnit> compilationUnits) {
		final List<JavaRoot> result = new ArrayList<>();

		for (final Entry<String, CompilationUnit> entry : compilationUnits.entrySet()) {
			final String sourceFilePath = entry.getKey();
			entry.getValue().accept(VISITOR);
			final JavaRoot root = VISITOR.getConvertedElement();
			Resource newResource;
			if (root.eResource() == null) {
				newResource = resourceSet.createResource(URI.createFileURI(sourceFilePath));
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
		for (final Resource res : new ArrayList<>(resourceSet.getResources())) {
			if (res.getContents().isEmpty()) {
				try {
					res.delete(resourceSet.getLoadOptions());
				} catch (final IOException e) {
					LOGGER.error(res.getURI(), e);
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean returnValue;
		if (this == obj) {
			returnValue = true;
		} else if (obj == null || getClass() != obj.getClass()) {
			returnValue = false;
		} else {
			final JaMoPPJDTParser other = (JaMoPPJDTParser) obj;
			returnValue = Objects.equals(resourceSet, other.resourceSet);
		}
		return returnValue;
	}

	@Override
	public <T> Set<T> get(final Class<T> type) {
		return resourceSet.getResources().stream().filter(Objects::nonNull)
				.filter(r -> !r.getContents().isEmpty() && !"file".equals(r.getURI().scheme()))
				.map(r -> r.getContents().get(0)).filter(Objects::nonNull).filter(type::isInstance).map(type::cast)
				.collect(Collectors.toSet());
	}

	@Override
	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	@Override
	public String[] getSourcepathEntries(final Path dir) {
		String[] returnValue;
		try (Stream<Path> paths = Files.walk(dir)) {
			returnValue = paths.filter(Files::isRegularFile).filter(this::isJavaFile).map(Path::toAbsolutePath)
					.map(Path::normalize).map(Path::toString).filter(p -> {
						final Resource resource = JavaClasspath.get().getResource(URI.createFileURI(p));
						if (resource != null) {
							resourceSet.getResources().add(resource);
							return false;
						}
						return true;
					}).toArray(i -> new String[i]);
		} catch (final IOException e) {
			LOGGER.error(dir, e);
			returnValue = new String[0];
		}
		return returnValue;
	}

	private boolean isJavaFile(final Path path) {
		final Path fileName = path.getFileName();
		if (fileName == null) {
			return false;
		}
		return fileName.toString().toLowerCase(Locale.US).endsWith("java");
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceSet);
	}

	@Override
	public JavaRoot parse(final String fileName, final InputStream input) {
		final Resource resource = JavaClasspath.get().getResource(URI.createFileURI(fileName));
		JavaRoot root;
		if (resource != null) {
			root = (JavaRoot) resource.getContents().get(0);
		} else {
			final StringBuilder builder = new StringBuilder();
			final String lineSep = System.lineSeparator();
			try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
				buffReader.lines().forEach(line -> builder.append(line).append(lineSep));
			} catch (final IOException e) {
				LOGGER.error(input, e);
			}
			final String src = builder.toString();
			final ASTNode ast = JAMOPP_FILE_WITH_JDT_PARSER.parseFileWithJDT(src, fileName);
			VISITOR.setSource(src);
			ast.accept(VISITOR);
			UTIL_TYPE_INSTRUCTION_SEPARATION.convertAll();
			UTIL_JDT_RESOLVER.completeResolution();

			root = VISITOR.getConvertedElement();
		}
		return root;
	}

	@Override
	public ResourceSet parseDirectory(final ASTParser parser, final Path dir) {
		final String[] sources = getSourcepathEntries(dir);
		final String[] encodings = new String[sources.length];
		Arrays.fill(encodings, DEFAULT_ENCODING);
		convertCompilationUnits(getCompilationUnits(parser, getClasspathEntries(dir), sources, encodings));

		return resourceSet;
	}

	@Override
	public ResourceSet parseDirectory(final Path dir) {
		return parseDirectory(getJavaParser(DEFAULT_JAVA_VERSION), dir);
	}

	@Override
	public Resource parseFile(final Path file) {
		Resource result = JavaClasspath.get().getResource(URI.createFileURI(file.toAbsolutePath().toString()));
		if (result == null) {
			result = convertCompilationUnits(getCompilationUnits(getJavaParser(DEFAULT_JAVA_VERSION), new String[] {},
					new String[] { file.toAbsolutePath().toString() }, new String[] { DEFAULT_ENCODING })).get(0)
					.eResource();

		}

		return result;

	}

	@Override
	public ResourceSet parsePackage(final IPackageFragment javaPackage) {
		final Map<String, CompilationUnit> compilationUnits = new ConcurrentHashMap<>();

		try {
			for (final ICompilationUnit unit : javaPackage.getCompilationUnits()) {
				if (unit instanceof CompilationUnit) {
					compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
				}

			}
		} catch (final JavaModelException e) {
			LOGGER.error(javaPackage, e);
		}

		convertCompilationUnits(compilationUnits);
		return resourceSet;
	}

	@Override
	public ResourceSet parseProject(final IJavaProject javaProject) {
		final Map<String, CompilationUnit> compilationUnits = new ConcurrentHashMap<>();

		try {
			for (final IPackageFragment mypackage : javaProject.getPackageFragments()) {
				for (final ICompilationUnit unit : mypackage.getCompilationUnits()) {
					if (unit instanceof CompilationUnit) {
						compilationUnits.put(unit.getPath().toString(), (CompilationUnit) unit);
					}

				}
			}
		} catch (final JavaModelException e) {
			LOGGER.error(javaProject, e);
		}

		convertCompilationUnits(compilationUnits);
		return resourceSet;
	}

	@Override
	public void setResourceSet(final ResourceSet set) {
		resourceSet = Objects.requireNonNull(set);
	}

	@Override
	public String toString() {
		return "JaMoPPJDTParser [resourceSet=" + resourceSet + "]";
	}

	public static String[] getClasspathEntries(final Path dir) {
		return JAMOPP_CLASSPATH_ENTRIES_SEARCHER.getClasspathEntries(dir);
	}

	public static Map<String, CompilationUnit> getCompilationUnits(final ASTParser parser,
			final String[] classpathEntries, final String[] sources, final String[] encodings) {
		return JAMOPP_COMPILATION_UNITS_FACTORY.getCompilationUnits(parser, classpathEntries, sources, encodings);
	}

	public static ASTParser getJavaParser(final String version) {
		return JAMOPP_JAVA_PARSER_FACTORY.getJavaParser(version);
	}

}