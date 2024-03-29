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

package tools.mdsd.jamopp.test.bugs;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import tools.mdsd.jamopp.test.AbstractJaMoPPTests;

public abstract class AbstractBugTestCase extends AbstractJaMoPPTests {
    protected ResourceSet createResourceSet() {
        final ResourceSet rs = new ResourceSetImpl();
        rs.getLoadOptions()
            .putAll(getLoadOptions());
        return rs;
    }

    @Override
    protected String getTestInputFolder() {
        return "src-input";
    }

    @Override
    protected boolean isExcludedFromReprintTest(String fileName) {
        return true;
    }
}
