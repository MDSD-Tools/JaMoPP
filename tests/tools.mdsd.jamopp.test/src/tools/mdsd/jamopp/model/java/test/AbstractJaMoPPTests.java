/*******************************************************************************
 * Copyright (c) 2006-2013 Software Technology Group, Dresden University of Technology DevBoost
 * GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Software Technology Group - TU Dresden, Germany; DevBoost GmbH - Berlin, Germany -
 * initial API and implementation
 ******************************************************************************/

package tools.mdsd.jamopp.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.text.edits.MalformedTreeException;
import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.commons.NamedElement;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.modifiers.Public;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import tools.mdsd.jamopp.resource.JavaResource2;
import tools.mdsd.jamopp.resource.JavaResource2Factory;

/**
 * Abstract superclass that provides some frequently used assert and helper methods.
 */
public abstract class AbstractJaMoPPTests {

    protected static final String TEST_OUTPUT_FOLDER = "output";

    protected static void assertClassifierName(Classifier declaration, String expectedName) {
        assertEquals(expectedName, declaration.getName(),
                "The name of the declared classifier should equal '" + expectedName + "'");
    }

    protected static void assertClassTypeParameterCount(Member member, int expectedNumberOfTypeArguments) {
        assertType(member, tools.mdsd.jamopp.model.java.classifiers.Class.class);
        final tools.mdsd.jamopp.model.java.classifiers.Class clazz = (tools.mdsd.jamopp.model.java.classifiers.Class) member;
        final List<TypeParameter> typeParameters = clazz.getTypeParameters();
        assertEquals(expectedNumberOfTypeArguments, typeParameters.size(),
                "Expected " + expectedNumberOfTypeArguments + " type parameter(s).");
    }

    protected static void assertConstructorThrowsCount(Member member, int expectedNumberOfThrownExceptions) {
        assertType(member, Constructor.class);
        final Constructor constructor = (Constructor) member;
        final List<NamespaceClassifierReference> exceptions = constructor.getExceptions();
        assertEquals(expectedNumberOfThrownExceptions, exceptions.size(),
                "Expected " + expectedNumberOfThrownExceptions + " exception(s).");
    }

    protected static void assertConstructorTypeParameterCount(Member member, int expectedNumberOfTypeArguments) {
        assertType(member, Constructor.class);
        final Constructor constructor = (Constructor) member;
        final List<TypeParameter> typeParameters = constructor.getTypeParameters();
        assertEquals(expectedNumberOfTypeArguments, typeParameters.size(),
                "Expected " + expectedNumberOfTypeArguments + " type parameter(s).");
    }

    protected static void assertInterfaceTypeParameterCount(Member member, int expectedNumberOfTypeArguments) {
        assertType(member, Interface.class);
        final Interface interfaze = (Interface) member;
        final List<TypeParameter> typeParameters = interfaze.getTypeParameters();
        assertEquals(expectedNumberOfTypeArguments, typeParameters.size(),
                "Expected " + expectedNumberOfTypeArguments + " type parameter(s).");
    }

    protected static void assertIsClass(Classifier classifier) {
        assertType(classifier, tools.mdsd.jamopp.model.java.classifiers.Class.class);
    }

    protected static Constructor assertIsConstructor(Member member) {
        assertType(member, Constructor.class);
        return (Constructor) member;
    }

    protected static void assertIsInterface(Classifier classifier) {
        assertType(classifier, Interface.class);
    }

    protected static void assertIsPublic(Method method) {
        assertTrue(method.getModifiers()
            .get(0) instanceof Public, "Method '" + method.getName() + "' should be public.");
    }

    protected static void assertMemberCount(MemberContainer container, int expectedCount) {
        String name = container.toString();
        if (container instanceof NamedElement) {
            name = ((NamedElement) container).getName();
        }
        assertEquals(expectedCount, container.getMembers()
            .size(), name + " should have " + expectedCount + " member(s).");
    }

    protected static void assertMethodThrowsCount(Member member, int expectedNumberOfThrownExceptions) {
        assertType(member, Method.class);
        final Method method = (Method) member;
        final List<NamespaceClassifierReference> exceptions = method.getExceptions();
        assertEquals(expectedNumberOfThrownExceptions, exceptions.size(),
                "Expected " + expectedNumberOfThrownExceptions + " exception(s).");
    }

    protected static void assertMethodTypeParameterCount(Member member, int expectedNumberOfTypeArguments) {
        assertType(member, Method.class);
        final Method method = (Method) member;
        final List<TypeParameter> typeParameters = method.getTypeParameters();
        assertEquals(expectedNumberOfTypeArguments, typeParameters.size(),
                "Expected " + expectedNumberOfTypeArguments + " type parameter(s).");
    }

    protected static void assertModelValid(Resource resource) {
        final org.eclipse.emf.common.util.Diagnostic result = Diagnostician.INSTANCE.validate(resource.getContents()
            .get(0));
        final StringBuilder msg = new StringBuilder("EMF validation problems found:");
        for (final org.eclipse.emf.common.util.Diagnostic childResult : result.getChildren()) {
            msg.append("\n")
                .append(childResult.getMessage());
        }
        assertTrue(result.getChildren()
            .isEmpty(), msg.toString());
    }

    protected static void assertModelValid(ResourceSet set) {
        final StringBuilder builder = new StringBuilder();
        for (final Resource res : new ArrayList<>(set.getResources())) {
            if (res.getContents()
                .size() == 0) {
                continue;
            }
            final org.eclipse.emf.common.util.Diagnostic result = Diagnostician.INSTANCE.validate(res.getContents()
                .get(0));
            if (!result.getChildren()
                .isEmpty()) {
                builder.append("EMF validation problems found in '");
                builder.append(res.getURI()
                    .toString());
                builder.append("':\n");
                for (final org.eclipse.emf.common.util.Diagnostic childResult : result.getChildren()) {
                    builder.append(childResult.getMessage());
                    builder.append("\n");
                }
            }
        }
        assertTrue(builder.length() == 0, builder.toString());
    }

    protected static void assertModifierCount(Method method, int expectedNumberOfModifiers) {
        assertEquals(expectedNumberOfModifiers, method.getModifiers()
            .size(), "Method '" + method.getName() + "' should have " + expectedNumberOfModifiers + " modifier(s).");
    }

    private static void assertNoErrors(String fileIdentifier, JavaResource2 resource) {
        final List<Diagnostic> errors = new BasicEList<>(resource.getErrors());
        printErrors(fileIdentifier, errors);
        assertTrue(errors.isEmpty(), "The resource should be parsed without errors.");
    }

    private static void assertNoWarnings(String fileIdentifier, JavaResource2 resource) {
        final List<Diagnostic> warnings = resource.getWarnings();
        printWarnings(fileIdentifier, warnings);
        assertTrue(warnings.isEmpty(), "The resource should be parsed without warnings.");
    }

    protected static void assertNumberOfClassifiers(CompilationUnit model, int expectedCount) {
        assertEquals(expectedCount, model.getClassifiers()
            .size(), "The compilation unit should contain " + expectedCount + " classifier declaration(s).");
    }

    protected static void assertResolveAllProxies(EObject element) {
        assertResolveAllProxies(element.eResource());
    }

    protected static boolean assertResolveAllProxies(Resource resource) {
        final StringBuilder msg = new StringBuilder();
        resource.getAllContents()
            .forEachRemaining(obj -> {
                final InternalEObject element = (InternalEObject) obj;
                assertFalse(element.eIsProxy(), "Can not resolve: " + element.eProxyURI());
                for (EObject crElement : element.eCrossReferences()) {
                    crElement = EcoreUtil.resolve(crElement, resource);
                    if (crElement.eIsProxy()) {
                        msg.append("\nCan not resolve: " + ((InternalEObject) crElement).eProxyURI());
                    }
                }
            });
        final String finalMsg = msg.toString();
        assertFalse(finalMsg.length() != 0, finalMsg);
        return finalMsg.length() != 0;
    }

    protected static void assertType(EObject object, Class<?> expectedType) {
        assertTrue(expectedType.isInstance(object),
                "The object should have type '" + expectedType.getSimpleName() + "', but was " + object.getClass()
                    .getSimpleName());
    }

    protected static String calculateOutputFilename(File inputFile, String inputFolderName, String outputFolderName) {
        final File inputPath = new File(".", inputFolderName);
        final int trimOffset = inputPath.getAbsolutePath()
            .length() - inputFolderName.length() - 2;
        final File outputFolder = new File(".", outputFolderName);
        final File outputFile = new File(outputFolder, inputFile.getAbsolutePath()
            .substring(trimOffset));
        return outputFile.getAbsolutePath();
    }

    protected static List<File> collectAllFilesRecursive(File startFolder, String extension) {
        if (!startFolder.isDirectory()) {
            return Collections.emptyList();
        }
        try (final Stream<Path> walk = Files.walk(startFolder.toPath())) {
            return walk.filter(p -> !p.endsWith(".svn"))
                .filter(p -> !p.endsWith(".git"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(f -> f.getName()
                    .endsWith(extension))
                .collect(Collectors.toList());
        } catch (final IOException e) {
            fail(e.getMessage());
            return Collections.emptyList();
        }
    }

    private static boolean compareTextContents(InputStream inputStream, boolean isModule, InputStream inputStream2,
            boolean is2Module) throws MalformedTreeException {

        final org.eclipse.jdt.core.dom.CompilationUnit unit1 = parseWithJDT(inputStream, isModule);
        removeJavadoc(unit1);

        final org.eclipse.jdt.core.dom.CompilationUnit unit2 = parseWithJDT(inputStream2, is2Module);
        removeJavadoc(unit2);

        final TalkativeASTMatcher matcher = new TalkativeASTMatcher(true);
        final boolean result = unit1.subtreeMatch(matcher, unit2);

        assertTrue(result, "Reprint not equal: " + matcher.getDiff());

        return result;
    }

    private static org.eclipse.jdt.core.dom.CompilationUnit parseWithJDT(InputStream inputStream, boolean isModule) {

        final ASTParser jdtParser = ASTParser.newParser(AST.getJLSLatest());
        final char[] charArray = readTextContents(inputStream).toCharArray();
        jdtParser.setSource(charArray);

        if (isModule) {
            jdtParser.setUnitName("module-info.java");
        }

        final Map<String, String> options = new HashMap<>();
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_14);
        jdtParser.setCompilerOptions(options);

        return (org.eclipse.jdt.core.dom.CompilationUnit) jdtParser.createAST(null);
    }

    protected static boolean prefixUsedInZipFile() {
        return false;
    }

    private static File prepareOutputFile(String outputFileName) {
        final File outputFile = new File(URI.createFileURI(outputFileName)
            .toFileString());
        final File parent = outputFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return outputFile;
    }

    private static void printDiagnostics(String filename, List<Diagnostic> errors, String diagnosticType) {
        if (errors.isEmpty()) {
            return;
        }

        final StringBuilder buffer = new StringBuilder();
        buffer.append(diagnosticType + " while parsing resource '" + filename + "':\n");
        for (final Diagnostic diagnostic : errors) {
            final String text = diagnostic.getMessage();
            buffer.append("\t" + text + "\n");
        }
        System.out.println(buffer.toString());
    }

    protected static void printErrors(String filename, List<Diagnostic> errors) {
        printDiagnostics(filename, errors, "Errors");
    }

    protected static void printWarnings(String filename, List<Diagnostic> warnings) {
        printDiagnostics(filename, warnings, "Warnings");
    }

    private static String readTextContents(InputStream inputStream) {
        final StringBuilder contents = new StringBuilder();
        try {
            final BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            try (input) {
                String line = null; // not declared within while loop
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.lineSeparator());
                }
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }

    public static void registerLibs(String libdir, JavaClasspath classpath, String prefix) {
        final File libFolder = new File("." + File.separator + libdir);
        final List<File> allLibFiles = collectAllFilesRecursive(libFolder, "jar");
        for (final File libFile : allLibFiles) {
            final String libPath = libFile.getAbsolutePath();
            URI.createFileURI(libPath);
        }
    }

    private static void removeJavadoc(org.eclipse.jdt.core.dom.CompilationUnit result1) {
        final List<Javadoc> javadocNodes = new ArrayList<>();
        result1.accept(new ASTVisitor() {
            @Override
            public boolean visit(Javadoc node) {
                javadocNodes.add(node);
                return true;
            }
        });
        for (final Javadoc node : javadocNodes) {
            node.delete();
        }
    }

    private ResourceSet testSet;

    protected void addFileToClasspath(File file, ResourceSet resourceSet) throws Exception {
        JavaClasspath.get();
        String fullName = file.getPath()
            .substring(getTestInputFolder().length() + 3, file.getPath()
                .length() - 5);
        fullName = fullName.replace(File.separator, ".");
        final int idx = fullName.lastIndexOf(".");
        if (idx != -1) {
            fullName.substring(0, idx);
            fullName.substring(idx + 1);
        }
    }

    protected Annotation assertParsesToAnnotation(String typename) throws Exception {
        return assertParsesToAnnotation("", typename);
    }

    protected Annotation assertParsesToAnnotation(String pkgFolder, String typename) throws Exception {
        return assertParsesToType(pkgFolder, typename, Annotation.class);
    }

    protected tools.mdsd.jamopp.model.java.classifiers.Class assertParsesToClass(String typename) throws Exception {
        return assertParsesToClass("", typename);
    }

    protected void assertParsesToClass(String typename, int expectedMembers) throws Exception {
        assertParsesToClass("", typename, expectedMembers);
    }

    protected tools.mdsd.jamopp.model.java.classifiers.Class assertParsesToClass(String pkgFolder, String typename)
            throws Exception {
        return assertParsesToType(pkgFolder, typename, tools.mdsd.jamopp.model.java.classifiers.Class.class);
    }

    protected void assertParsesToClass(String pkgFolder, String typename, int expectedMembers) throws Exception {
        final String filename = typename + ".java";
        final tools.mdsd.jamopp.model.java.classifiers.Class clazz = assertParsesToClass(pkgFolder, typename);
        assertEquals(expectedMembers, clazz.getMembers()
            .size(), typename + " should have " + expectedMembers + " member(s).");

        parseAndReprint(filename);
    }

    protected Enumeration assertParsesToEnumeration(String typename) throws Exception {
        return assertParsesToEnumeration("", typename);
    }

    protected Enumeration assertParsesToEnumeration(String pkgFolder, String typename) throws Exception {
        return assertParsesToType(pkgFolder, typename, Enumeration.class);
    }

    protected Interface assertParsesToInterface(String typename) throws Exception {
        return assertParsesToInterface("", typename);
    }

    protected Interface assertParsesToInterface(String pkgFolder, String typename) throws Exception {
        return assertParsesToType(pkgFolder, typename, Interface.class);
    }

    protected <T> T assertParsesToType(String typename, Class<T> expectedType) throws Exception {
        return assertParsesToType("", typename, expectedType);
    }

    protected <T> T assertParsesToType(String pkgFolder, String typename, Class<T> expectedType) throws Exception {
        return assertParsesToType(pkgFolder, typename, getTestInputFolder(), expectedType);
    }

    protected <T> T assertParsesToType(String pkgFolder, String typename, String folder, Class<T> expectedType)
            throws Exception {
        final String filename = pkgFolder + File.separator + typename + ".java";
        final JavaRoot model = parseResource(filename, folder);
        if (model instanceof CompilationUnit cu) {
            assertNumberOfClassifiers(cu, 1);

            final Classifier declaration = cu.getClassifiers()
                .get(0);
            assertClassifierName(declaration, typename);
            assertType(declaration, expectedType);
            return expectedType.cast(declaration);
        }
        return null;
    }

    private void createNewResourceSet() {
        testSet = new ResourceSetImpl();
        testSet.getLoadOptions()
            .putAll(getLoadOptions());
    }

    protected Map<Object, Object> getLoadOptions() {
        return new HashMap<>();
    }

    protected ResourceSet getResourceSet() {
        return testSet;
    }

    protected abstract String getTestInputFolder();

    protected boolean ignoreSemanticErrors(String filename) {
        return false;
    }

    @BeforeEach
    public final void initResourceFactory() {
        createNewResourceSet();
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
            .put("java", new JavaResource2Factory());
        JavaClasspath.get()
            .clear();
    }

    protected abstract boolean isExcludedFromReprintTest(String filename);

    protected JavaRoot loadResource(String filePath) throws Exception {
        final URI uri = URI.createFileURI(filePath);
        return loadResource(uri);
    }

    protected JavaRoot loadResource(URI uri) throws Exception {
        final JavaResource2 resource = (JavaResource2) getResourceSet().createResource(uri);
        resource.load(getLoadOptions());

        assertNoErrors(uri.toString(), resource);
        assertNoWarnings(uri.toString(), resource);

        final EList<EObject> contents = resource.getContents();
        assertEquals(1, contents.size(), "The resource must have one content element.");

        final EObject content = contents.get(0);
        assertTrue(content instanceof JavaRoot, "File '" + uri.toString() + "' was parsed to JavaRoot.");
        return (JavaRoot) content;
    }

    protected void parseAndReprint(File file) throws Exception {
        parseAndReprint(file, getTestInputFolder(), TEST_OUTPUT_FOLDER);
    }

    protected void parseAndReprint(File file, String inputFolderName, String outputFolderName) throws Exception {
        final File inputFile = file;
        assertTrue(inputFile.exists(), "File " + inputFile.getAbsolutePath() + " exists.");

        final Resource resource = getResourceSet().createResource(URI.createFileURI(inputFile.getAbsolutePath()));
        resource.load(getLoadOptions());

        testReprint((JavaResource2) resource, outputFolderName);
    }

    protected void parseAndReprint(String filename) throws Exception {
        parseAndReprint(filename, getTestInputFolder(), TEST_OUTPUT_FOLDER);
    }

    protected void parseAndReprint(String filename, String inputFolderName, String outputFolderName) throws Exception {
        final String path = "." + File.separator + inputFolderName + File.separator + filename;
        final File file = new File(path);
        parseAndReprint(file, inputFolderName, outputFolderName);
    }

    protected void parseAndReprint(ZipFile file, ZipEntry entry, String outputFolderName, String libFolderName)
            throws Exception {
        final URI archiveURI = URI.createURI("archive:file:///" + new File(".").getAbsoluteFile()
            .toURI()
            .getRawPath()
                + file.getName()
                    .replace('\\', '/')
                + "!/" + entry.getName());

        final ResourceSet resourceSet = getResourceSet();

        if (prefixUsedInZipFile()) {
            final String prefix = entry.getName()
                .substring(0, entry.getName()
                    .indexOf("/") + 1);
            registerLibs(libFolderName, JavaClasspath.get(), prefix);
        }

        final Resource resource = resourceSet.createResource(archiveURI);
        resource.load(getLoadOptions());

        if (!ignoreSemanticErrors(entry.getName())) {
            // This will not work if external resources are not yet registered (order of tests)
            assertResolveAllProxies(resource);
        }

        if (isExcludedFromReprintTest(entry.getName())) {
            return;
        }

        final String entryName = entry.getName();
        final String outputFileName = "./" + outputFolderName + File.separator + entryName;
        final File outputFile = prepareOutputFile(outputFileName);
        resource.setURI(URI.createFileURI(outputFile.getAbsolutePath()));
        resource.save(null);

        assertTrue(outputFile.exists(), "File " + outputFile.getAbsolutePath() + " must exist.");
        compareTextContents(file.getInputStream(entry), entryName.endsWith("module-info.java"),
                new FileInputStream(outputFile), outputFile.getPath()
                    .endsWith("module-info.java"));
    }

    protected JavaRoot parseResource(String filename) throws Exception {
        return parseResource(filename, getTestInputFolder());
    }

    protected JavaRoot parseResource(String filename, String inputFolderName) throws Exception {
        final File inputFolder = new File("." + File.separator + inputFolderName);
        final File file = new File(inputFolder, filename);
        assertTrue(file.exists(), "File " + file + " must exist.");

        return loadResource(file.getAbsolutePath());
    }

    protected JavaRoot parseResource(ZipFile file, ZipEntry entry) throws Exception {
        return loadResource(URI.createURI("archive:file:///" + new File(".").getAbsoluteFile()
            .toURI()
            .getRawPath()
                + file.getName()
                    .replace('\\', '/')
                + "!/" + entry.getName()));
    }

    protected void registerInClassPath(String file) throws Exception {
        final String inputFolderPath = "." + File.separator + getTestInputFolder();
        final File inputFolder = new File(inputFolderPath);
        File inputFile = new File(file);

        parseResource(inputFile.getPath());

        inputFile = new File(inputFolder + File.separator + file);
    }

    @AfterEach
    public void tearDown() {
        for (final Resource res : new ArrayList<>(testSet.getResources())) {
            res.unload();
            testSet.getResources()
                .remove(res);
        }
    }

    protected void testReprint(JavaResource2 resource) throws Exception {
        testReprint(resource, TEST_OUTPUT_FOLDER);
    }

    protected void testReprint(JavaResource2 resource, String outputFolderName) throws Exception {
        final String inputFile = resource.getURI()
            .toFileString();
        if (inputFile == null) {
            return;
        }
        assertNoErrors(resource.getURI()
            .toString(), resource);

        if (!ignoreSemanticErrors(inputFile)) {
            // This will not work if external resources are not yet registered (order of tests)
            assertResolveAllProxies(resource);
            // Default EMF validation should not fail
            assertModelValid(resource);
        }

        if (isExcludedFromReprintTest(inputFile)) {
            return;
        }

        final Path input = Paths.get(inputFile);
        final Path localDir = Paths.get(".")
            .toAbsolutePath();
        final String outputFileName = calculateOutputFilename(input.toFile(), localDir.relativize(input)
            .getName(0)
            .toString(), outputFolderName);
        resource.setURI(URI.createFileURI(outputFileName));
        resource.save(null);

        final File outputFile = prepareOutputFile(outputFileName);
        assertTrue(outputFile.exists(), "File " + outputFile.getAbsolutePath() + " exists.");

        try (final FileInputStream inputStream = new FileInputStream(inputFile)) {
            compareTextContents(inputStream, inputFile.endsWith("module-info.java"), new FileInputStream(outputFile),
                    outputFile.getPath()
                        .endsWith("module-info.java"));
        }
    }

    protected void testReprint(ResourceSet set) throws Exception {
        for (final Resource res : set.getResources()) {
            if (res instanceof JavaResource2) {
                testReprint((JavaResource2) res);
            }
        }
    }
}
