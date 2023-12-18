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

package tools.mdsd.jamopp.model.java.test.xmi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.eclipse.emf.ecore.xmi.XMIResource;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.containers.Package;
import tools.mdsd.jamopp.model.java.test.AbstractJaMoPPTests;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

@Disabled("xmiResource is null in line 106.")
public class JavaXMISerializationTest extends AbstractJaMoPPTests {

    protected static final String TEST_INPUT_FOLDER_NAME = "src-input";
    protected static final String TEST_OUTPUT_FOLDER_NAME = "output";
    private final HashMap<String, String> inputFileToOutputFile = new HashMap<>();

    private ResourceSet sharedRS;

    protected void compare(File file) throws Exception {
        final ResourceSet rs = getResourceSet();
        final String outputXMIFileName = inputFileToOutputFile.get(file.getAbsolutePath());
        final URI xmiFileURI = URI.createFileURI(outputXMIFileName)
            .trimFileExtension()
            .appendFileExtension("xmi");
        final Resource xmiResource = rs.getResource(xmiFileURI, false);
        if (xmiResource == null) {
            System.out.print("");
        }
        assertNotNull(xmiResource);

        // reload
        final ResourceSet reloadeSet = super.getResourceSet();
        Resource reloadedResource = null;
        try {
            reloadedResource = reloadeSet.getResource(xmiFileURI, true);
        } catch (final Exception e) {
            fail(e.getClass() + ": " + e.getMessage());
            return;
        }
        assertResolveAllProxies(reloadedResource);
        for (final Diagnostic d : reloadedResource.getErrors()) {
            System.out.println(d.getMessage());
        }
        assertTrue(reloadedResource.getErrors()
            .isEmpty(), "Parsed XMI contains errors");

        final EqualityHelper equalityHelper = new EqualityHelper() {

            private static final long serialVersionUID = 4881383880532985929L;

            @Override
            public boolean equals(EObject eObject1, EObject eObject2) {
                final boolean result = super.equals(eObject1, eObject2);
                if (!result) {
                    System.out.println("Not equal: " + eObject1 + " != " + eObject2);
                }
                return result;
            }

            @Override
            protected boolean haveEqualFeature(EObject eObject1, EObject eObject2, EStructuralFeature feature) {
                if (feature.isTransient()) {
                    // ignore transient features
                    return true;
                }
                return super.haveEqualFeature(eObject1, eObject2, feature);
            }
        };
        final EObject root = xmiResource.getContents()
            .get(0);
        final EObject reloadedRoot = reloadedResource.getContents()
            .get(0);
        assertTrue(equalityHelper.equals(root, reloadedRoot), "Original and reloaded XMI are not equal");
    }

    @Override
    protected ResourceSet getResourceSet() {
        if (sharedRS == null) {
            sharedRS = super.getResourceSet();
        }
        return sharedRS;
    }

    @Override
    protected String getTestInputFolder() {
        return TEST_INPUT_FOLDER_NAME;
    }

    @Override
    protected boolean isExcludedFromReprintTest(String filename) {
        return true;
    }

    @Test
    public void testXMISerialization() throws Exception {
        final File inputFolder = new File("./" + TEST_INPUT_FOLDER_NAME);
        final List<File> allTestFiles = collectAllFilesRecursive(inputFolder, "java");

        final JaMoPPJDTParser parser = new JaMoPPJDTParser();
        parser.setResourceSet(getResourceSet());
        parser.parseDirectory(inputFolder.toPath());

        transferToXMI();

        for (final File file : allTestFiles) {
            compare(file);
        }

        inputFileToOutputFile.clear();
    }

    private void transferToXMI() throws Exception {
        final ResourceSet rs = getResourceSet();
        EcoreUtil.resolveAll(rs);
        int emptyFileName = 0;

        for (final Resource javaResource : new ArrayList<>(rs.getResources())) {
            assertResolveAllProxies(javaResource);
            if (javaResource.getContents()
                .isEmpty()) {
                System.out.println("WARNING: Emtpy Resource: " + javaResource.getURI());
                continue;
            }
            final JavaRoot root = (JavaRoot) javaResource.getContents()
                .get(0);
            String outputFileName = "ERROR";
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
            } else {
                fail();
            }
            final File outputFile = new File(
                    "." + File.separator + TEST_OUTPUT_FOLDER_NAME + File.separator + outputFileName);
            final URI xmiFileURI = URI.createFileURI(outputFile.getAbsolutePath())
                .appendFileExtension("xmi");
            final XMIResource xmiResource = (XMIResource) rs.createResource(xmiFileURI);
            xmiResource.setEncoding(StandardCharsets.UTF_8.toString());
            xmiResource.getContents()
                .addAll(javaResource.getContents());

            if (javaResource.getURI()
                .isFile()) {
                inputFileToOutputFile.put(javaResource.getURI()
                    .toFileString(), outputFile.getAbsolutePath());
            }
        }
        for (final Resource xmiResource : rs.getResources()) {
            if (xmiResource instanceof XMIResource) {
                try {
                    xmiResource.save(rs.getLoadOptions());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
