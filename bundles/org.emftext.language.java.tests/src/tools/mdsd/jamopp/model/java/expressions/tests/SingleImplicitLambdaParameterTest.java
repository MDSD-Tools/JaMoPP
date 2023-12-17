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
package tools.mdsd.jamopp.model.java.expressions.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.SingleImplicitLambdaParameter;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Single Implicit Lambda Parameter</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class SingleImplicitLambdaParameterTest extends ImplicitlyTypedLambdaParametersTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(SingleImplicitLambdaParameterTest.class);
	}

	/**
	 * Constructs a new Single Implicit Lambda Parameter test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SingleImplicitLambdaParameterTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Single Implicit Lambda Parameter test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected SingleImplicitLambdaParameter getFixture() {
		return (SingleImplicitLambdaParameter)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(ExpressionsFactory.eINSTANCE.createSingleImplicitLambdaParameter());
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

} //SingleImplicitLambdaParameterTest
