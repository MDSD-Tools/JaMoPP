/**
 * Copyright (c) 2006-2014
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 *   Martin Armbruster
 *      - Extension for Java 7-13
 *  
 */
package tools.mdsd.jamopp.model.java.references.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.references.TextBlockReference;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Text Block Reference</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class TextBlockReferenceTest extends ReferenceTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(TextBlockReferenceTest.class);
	}

	/**
	 * Constructs a new Text Block Reference test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TextBlockReferenceTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Text Block Reference test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected TextBlockReference getFixture() {
		return (TextBlockReference)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ReferencesFactory.eINSTANCE.createTextBlockReference());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //TextBlockReferenceTest
