/*******************************************************************************
 * Copyright 2021 Marvin Meller
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Marvin Meller - initial implementation Yves Kirschner - parameterize implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.test.standalone;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.containers.Package;
import tools.mdsd.jamopp.model.java.containers.impl.CompilationUnitImpl;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.test.resolving.ResolvingTest;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

/**
 * Parameterized test for standalone use of JDT-based conversion of Java source code to EMF-based
 * models. Apart from JUint, no dependencies to other tools were newly introduced. For this purpose,
 * the existing code was taken almost identically and copied into this test. This standalone
 * implementation worked as expected in the first version we developed together. A comparable
 * standalone call is also included in the original JaMoPP from DevBoost.
 *
 * @author Marvin Meller
 * @author Yves Kirschner
 *
 * @version 1.2
 */
public class StandaloneTest extends ResolvingTest {

    /**
     * Creates the path including the package hierarchy for the xmi output. Taken directly from the
     * last version.
     *
     * @param javaResource
     *     Resource for which the path is to be created.
     *
     * @return path including the package hierarchy
     *
     * @see tools.mdsd.jamopp.standalone.JaMoPPStandalone#checkScheme()
     */
    private static String checkScheme(Resource javaResource) {
        int emptyFileName = 0;
        String outputFileName = "";
        JavaRoot root = ContainersFactory.eINSTANCE.createEmptyModel();

        root = (JavaRoot) javaResource.getContents()
            .get(0);

        if (root instanceof CompilationUnit) {
            outputFileName = root.getNamespacesAsString()
                .replace(".", File.separator) + File.separator;
            final CompilationUnit cu = (CompilationUnit) root;
            if (!cu.getClassifiers()
                .isEmpty()) {
                outputFileName += cu.getClassifiers()
                    .get(0)
                    .getName();
            } else {
                outputFileName += emptyFileName;
                emptyFileName++;
            }

        } else if (root instanceof Package) {
            outputFileName = root.getNamespacesAsString()
                .replace(".", File.separator) + File.separator + "package-info";
            if (outputFileName.startsWith(File.separator)) {
                outputFileName = outputFileName.substring(1);
            }
        } else if (root instanceof tools.mdsd.jamopp.model.java.containers.Module) {
            outputFileName = root.getNamespacesAsString()
                .replace(".", File.separator) + File.separator + "module-info";
        }
        return outputFileName;
    }

    // @TestWithInput
    // public void testResourceSetSerialization(String input) {
    // // https://sdqweb.ipd.kit.edu/wiki/Creating_EMF_Model_instances_programmatically
    // // https://stackoverflow.com/questions/25864816/saving-an-emf-model
    //
    // final Path directory = directoryOf(input);
    // final ResourceSet resourceSet = assertDoesNotThrow(() -> new
    // JaMoPPJDTParser().parseDirectory(directory));
    // assertNotNull(resourceSet);
    // assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet));
    //
    // for (final Resource javaResource : new ArrayList<>(resourceSet.getResources())) {
    // // ENABLE_OUTPUT_OF_LIBRARY_FILES has been removed because no library files are
    // // to be output for the the moment.
    // if (javaResource.getContents().isEmpty() || !"file".equals(javaResource.getURI().scheme())) {
    // continue;
    // }
    //
    // final Resource xmiResource = resourceSet.createResource(outputUri(input, javaResource));
    // xmiResource.getContents().addAll(javaResource.getContents());
    // }
    //
    // for (final Resource resource : resourceSet.getResources()) {
    // if (resource instanceof XMIResource) {
    // assertTrue(resource.getAllContents().hasNext());
    // assertDoesNotThrow(() -> resource.save(resourceSet.getLoadOptions()));
    // }
    // }
    //
    // final ResourceSet resourceSetReloaded = new ResourceSetImpl();
    //
    // for (final Resource resource : resourceSet.getResources()) {
    // if (resource instanceof XMIResource) {
    // final Resource resourceReloaded = resourceSetReloaded.createResource(outputUri(input,
    // resource));
    // assertDoesNotThrow(() -> resourceReloaded.load(null));
    // assertNotSame(resource, resourceReloaded);
    // compareResources(resource, resourceReloaded);
    // }
    // }
    // }

    private static void compareClassifiers(ConcreteClassifier classifier1, ConcreteClassifier classifier2) {
        System.out.println("Classifier name: " + classifier1.getName());
        // assertEquals(classifier1.getPackage().getNamespaces(),
        // classifier2.getPackage().getNamespaces());
        assertEquals(classifier1.getName(), classifier2.getName());
        compareLists(Member::getName, classifier1.getMembers(), classifier2.getMembers(),
                StandaloneTest::compareMembers);
    }

    /**
     * Compare two lists, possibly in different order. Values are associated by their mapped keys
     * and then values with equal keys are compared. Asserts that the lists' mapped key sets are
     * equal.
     */
    private static <K, V> void compareLists(Function<V, K> keyMapper, List<V> values1, List<V> values2,
            BiConsumer<V, V> compare) {
        final Map<K, V> map1 = values1.stream()
            .collect(Collectors.toMap(keyMapper, value -> value));
        final Map<K, V> map2 = values2.stream()
            .collect(Collectors.toMap(keyMapper, value -> value));
        assertEquals(map1.keySet(), map2.keySet());
        map1.keySet()
            .forEach(key -> compare.accept(map1.get(key), map2.get(key)));
    }

    private static void compareMembers(Member member1, Member member2) {
        System.out.println("Member name: " + member1.getName());
        assertEquals(member1.getName(), member2.getName());
        assertEquals(member1.getClass(), member2.getClass());
    }

    private static void compareResources(Resource resource1, Resource resource2) {
        System.out.println("Resource URI: " + resource1.getURI()
            .lastSegment());
        final List<CompilationUnitImpl> units1 = getUnits(resource1);
        final List<CompilationUnitImpl> units2 = getUnits(resource2);
        assertEquals(1, units1.size());
        assertEquals(1, units2.size());
        compareUnits(units1.get(0), units2.get(0));
        // compareLists(CompilationUnit::getNamespaces, getUnits(resource1), getUnits(resource2),
        // StandaloneTest::compareUnits);
    }

    private static void compareUnits(CompilationUnitImpl unit1, CompilationUnitImpl unit2) {
        System.out.println("Unit namespaces: " + unit1.getNamespaces());
        // assertEquals(unit1.getNamespaces(), unit2.getNamespaces());
        compareLists(ConcreteClassifier::getQualifiedName, unit1.getClassifiers(), unit2.getClassifiers(),
                StandaloneTest::compareClassifiers);
    }

    private static List<CompilationUnitImpl> getUnits(Resource resource) {
        // TODO filter getName() != null relevant?
        // See
        // https://github.com/PalladioSimulator/Palladio-ReverseEngineering-SoMoX-JaMoPP/blob/1bdf3c37c211676637ac93318c3fcf800b9de955/bundles/org.palladiosimulator.somox.analyzer.rules.main/src/org/palladiosimulator/somox/analyzer/rules/main/RuleEngine.java#L175
        return resource.getContents()
            .stream()
            .filter(CompilationUnitImpl.class::isInstance)
            .map(CompilationUnitImpl.class::cast)
            .filter(compi -> compi.getName() != null)
            .collect(Collectors.toList());
    }

    private static URI outputUri(String input, Resource resource) {
        // For the parameterized test, the input parameter was also included in the path
        // for the output.
        final File outputFile = new File(
                "standalone_output" + File.separator + input + File.separator + checkScheme(resource));
        return URI.createFileURI(outputFile.getAbsolutePath())
            .appendFileExtension("xmi");
    }
    
    /**
     * This test generates corresponding EMF resources for the Java files in a project. JUinit
     * passes the folder name of the projects {@code <project>} to be tested as a parameter. The EMF
     * sources are then stored as XMI files in the folder {@code ./standalone_output/<project>/}.
     *
     * This {@code ./standalone_output/} folder is not yet automatically deleted after test
     * execution. Likewise, these saved EMF resources would have to be reloaded and then tested even
     * further. However, since there are reproducible errors before, these should be fixed before
     * reasonable testing can continue.
     *
     * As before, the projects do not need to be built. The projects are integrated with Git
     * submodules, so with {@code git submodule update --init} all projects must be updated before
     * test execution.
     *
     *
     * @param input
     *     Name of the project folder to test.
     *
     * @see tools.mdsd.jamopp.standalone.JaMoPPStandalone
     * @see tools.mdsd.jamopp.model.java.test.AbstractJaMoPPTests
     */
    private void resourceSerialization(String input) {
        final Path directory = directoryOf(input);
        final ResourceSet resourceSet = assertDoesNotThrow(() -> new JaMoPPJDTParser().parseDirectory(directory));
        assertNotNull(resourceSet);
        assertDoesNotThrow(() -> EcoreUtil.resolveAll(resourceSet));

        for (final Resource javaResource : new ArrayList<>(resourceSet.getResources())) {
            // ENABLE_OUTPUT_OF_LIBRARY_FILES has been removed because no library files are
            // to be output for the the moment.
            if (javaResource.getContents()
                .isEmpty()
                    || !"file".equals(javaResource.getURI()
                        .scheme())) {
                continue;
            }

            final Resource xmiResource = resourceSet.createResource(outputUri(input, javaResource));
            xmiResource.getContents()
                .addAll(javaResource.getContents());
        }

        // URI uri = URI.createFileURI("todo-test");
        // Resource resource = resourceSet.createResource(uri);
        // assertDoesNotThrow(() -> resource.save(((XMLResource)
        // resource).getDefaultSaveOptions()));
        //
        // final ResourceSet resourceSetReloaded = new ResourceSetImpl();
        // assertDoesNotThrow(() -> resourceSetReloaded.getResource(uri, true));

        // final ResourceSet resourceSetReloaded = new ResourceSetImpl();
        //
        // Resource resourceReloaded = resourceSetReloaded.createResource(uri);
        // // ((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<>());
        // resourceReloaded.load(((XMLResource) resourceReloaded).getDefaultLoadOptions());

        for (final Resource resource : resourceSet.getResources()) {
            if (resource instanceof XMIResource) {
                assertTrue(resource.getAllContents()
                    .hasNext());
                assertDoesNotThrow(() -> resource.save(resourceSet.getLoadOptions()));
                assertDoesNotThrow(() -> resource.save(resourceSet.getLoadOptions()));
            }
        }

        final ResourceSet resourceSetReloaded = new ResourceSetImpl();

        for (final Resource resource : resourceSet.getResources()) {
            if (resource instanceof XMIResource) {
                final Resource resourceReloaded = resourceSetReloaded.createResource(outputUri(input, resource));
                assertDoesNotThrow(() -> resourceReloaded.load(null));
                assertNotSame(resource, resourceReloaded);
                compareResources(resource, resourceReloaded);
            }
        }
    }
    
    @Test
    public void testParameterizedResourceSerialization() {
    	assertDoesNotThrow(() -> resourceSerialization("esda-master"));
    	assertDoesNotThrow(() -> resourceSerialization("trojan-source-main"));
    	assertDoesNotThrow(() -> resourceSerialization("spring-petclinic-microservices-master"));
    }
}
