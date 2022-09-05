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

package org.emftext.language.java.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.Test;

import jamopp.resource.JavaResource2;

/**
 * A separate test case for the input files that contain Unicode escape sequences.
 */
public class UnicodeTest extends AbstractJaMoPPTests {

    protected void assertParsesWithoutErrors(String typename, Map<?, ?> loadOptions) throws Exception {
        final String filename = File.separator + typename + ".java";
        final File inputFolder = new File("./" + getTestInputFolder());
        final File file = new File(inputFolder, filename);
        assertTrue(file.exists(), "File " + file + " should exist.");
        final URI fileURI = URI.createFileURI(file.getAbsolutePath());
        final JavaResource2 resource = (JavaResource2) getResourceSet().createResource(fileURI);
        resource.load(loadOptions);

        assertTrue(resource.getErrors()
            .isEmpty());
    }

    @Override
    protected String getTestInputFolder() {
        return "src-input" + File.separator + "unicode";
    }

    @Override
    protected boolean isExcludedFromReprintTest(String filename) {
        return true;
    }

    @Test
    public void testUnicodeConverterDeactivated() {
        try {
            final Map<String, Object> loadOptions = Collections.emptyMap();

            assertParsesWithoutErrors("ControlZ", loadOptions);
            assertParsesWithoutErrors("Unicode", loadOptions);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUnicodeInput() {
        try {
            assertParsesToClass("ControlZ");
            assertParsesToClass("Unicode");
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
