/*******************************************************************************
 * Copyright (c) 2006-2014 Software Technology Group, Dresden University of Technology DevBoost
 * GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Software Technology Group - TU Dresden, Germany; DevBoost GmbH - Berlin, Germany -
 * initial API and implementation
 ******************************************************************************/

package tools.mdsd.jamopp.test.bugs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import tools.mdsd.jamopp.model.java.classifiers.ClassifiersFactory;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import org.junit.jupiter.api.Test;

public class Bug1819Test extends AbstractBugTestCase {
    @Test
    public void testDefaultPackage() throws IOException {
        final String className = "TestName";

        final ResourceSet rs = createResourceSet();
        final Resource r = rs.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Class", null,
                new String[] { className + ".java" }, null, null));
        final CompilationUnit cu = ContainersFactory.eINSTANCE.createCompilationUnit();
        final tools.mdsd.jamopp.model.java.classifiers.Class clazz = ClassifiersFactory.eINSTANCE.createClass();
        clazz.setName(className);
        cu.getClassifiers()
            .add(clazz);

        r.getContents()
            .add(cu);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        r.save(os, null);
        final String result = new String(os.toByteArray()).trim();

        assertTrue(result.matches("class\\s++" + className + "\\s++\\u007b\\s++\\u007d\\s*+"));
    }

    @Test
    public void testMissingDeclaredPackage() throws IOException {
        final String className = "TestName";
        final String packageName = "a.b.c";

        final ResourceSet rs = createResourceSet();
        final Resource resource = rs.createResource(URI.createHierarchicalURI("empty", "JaMoPP-Class", null,
                new String[] { packageName + "." + className + ".java" }, null, null));
        final CompilationUnit cu = ContainersFactory.eINSTANCE.createCompilationUnit();
        cu.getNamespaces()
            .add("a");
        cu.getNamespaces()
            .add("b");
        cu.getNamespaces()
            .add("c");
        final tools.mdsd.jamopp.model.java.classifiers.Class clazz = ClassifiersFactory.eINSTANCE.createClass();
        clazz.setName(className);
        cu.getClassifiers()
            .add(clazz);

        resource.getContents()
            .add(cu);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        resource.save(os, null);
        final String result = new String(os.toByteArray()).trim();

        assertTrue(result.matches("package\\s++" + packageName + "\\u003b\\s++" + "class\\s++" + className
                + "\\s++\\u007b\\s++\\u007d\\s*+"));
    }
}
