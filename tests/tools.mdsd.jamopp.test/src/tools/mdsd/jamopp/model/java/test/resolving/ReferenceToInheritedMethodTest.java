/*******************************************************************************
 * Copyright (c) 2006-2012 Software Technology Group, Dresden University of Technology DevBoost
 * GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Software Technology Group - TU Dresden, Germany; DevBoost GmbH - Berlin, Germany -
 * initial API and implementation
 ******************************************************************************/
package tools.mdsd.jamopp.test.resolving;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.jupiter.api.Test;

/**
 * A test for resolving method calls to the respective inherited method.
 */
public class ReferenceToInheritedMethodTest extends AbstractResolverTestCase {

    // Use a single resource set because resources
    // needs to know each other in this test.
    private final ResourceSet rs = new ResourceSetImpl();

    @Override
    protected ResourceSet getResourceSet() {
        rs.getLoadOptions()
            .putAll(getLoadOptions());
        return rs;
    }

    @Test
    public void testReferencing() throws Exception {
        testReferencing("MethodCallsWithoutInheritance");
        testReferencing("ReferenceToInheritedMethod");
    }

    protected void testReferencing(String typename) throws Exception {
        final String filename = typename + ".java";
        parseAndReprint(filename);
    }
}
