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

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>references</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class ReferencesTests extends TestSuite {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Test suite() {
		TestSuite suite = new ReferencesTests("references Tests");
		suite.addTestSuite(IdentifierReferenceTest.class);
		suite.addTestSuite(MethodCallTest.class);
		suite.addTestSuite(ReflectiveClassReferenceTest.class);
		suite.addTestSuite(PrimitiveTypeReferenceTest.class);
		suite.addTestSuite(StringReferenceTest.class);
		suite.addTestSuite(SelfReferenceTest.class);
		suite.addTestSuite(TextBlockReferenceTest.class);
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferencesTests(String name) {
		super(name);
	}

} //ReferencesTests
