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
package tools.mdsd.jamopp.model.java.generics.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.model.java.annotations.tests.AnnotableTest;

import tools.mdsd.jamopp.model.java.generics.GenericsFactory;
import tools.mdsd.jamopp.model.java.generics.SuperTypeArgument;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Super Type Argument</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link tools.mdsd.jamopp.model.java.arrays.ArrayTypeable#getArrayDimension() <em>Get Array Dimension</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class SuperTypeArgumentTest extends AnnotableTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(SuperTypeArgumentTest.class);
	}

	/**
	 * Constructs a new Super Type Argument test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SuperTypeArgumentTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Super Type Argument test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected SuperTypeArgument getFixture() {
		return (SuperTypeArgument)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(GenericsFactory.eINSTANCE.createSuperTypeArgument());
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

	/**
	 * Tests the '{@link tools.mdsd.jamopp.model.java.arrays.ArrayTypeable#getArrayDimension() <em>Get Array Dimension</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.mdsd.jamopp.model.java.arrays.ArrayTypeable#getArrayDimension()
	 * @generated
	 */
	public void testGetArrayDimension() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

} //SuperTypeArgumentTest
