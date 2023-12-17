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
package tools.mdsd.jamopp.commons.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.commons.layout.KeywordLayoutInformation;
import tools.mdsd.jamopp.commons.layout.LayoutFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Keyword Layout Information</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class KeywordLayoutInformationTest extends LayoutInformationTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(KeywordLayoutInformationTest.class);
	}

	/**
	 * Constructs a new Keyword Layout Information test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KeywordLayoutInformationTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Keyword Layout Information test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected KeywordLayoutInformation getFixture() {
		return (KeywordLayoutInformation)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(LayoutFactory.eINSTANCE.createKeywordLayoutInformation());
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

} //KeywordLayoutInformationTest
