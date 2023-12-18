/**
 * Copyright (c) 2006-2012
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
 *  
 */
package tools.mdsd.jamopp.commons.layout.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.commons.layout.AttributeLayoutInformation;
import tools.mdsd.jamopp.commons.layout.LayoutFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Attribute Layout Information</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AttributeLayoutInformationTest extends LayoutInformationTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(AttributeLayoutInformationTest.class);
	}

	/**
	 * Constructs a new Attribute Layout Information test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AttributeLayoutInformationTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Attribute Layout Information test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected AttributeLayoutInformation getFixture() {
		return (AttributeLayoutInformation)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(LayoutFactory.eINSTANCE.createAttributeLayoutInformation());
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

} //AttributeLayoutInformationTest
