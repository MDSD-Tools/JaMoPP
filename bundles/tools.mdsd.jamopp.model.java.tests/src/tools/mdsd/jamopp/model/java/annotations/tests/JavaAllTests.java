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
package tools.mdsd.jamopp.model.java.annotations.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.model.java.arrays.tests.ArraysTests;

import tools.mdsd.jamopp.model.java.classifiers.tests.ClassifiersTests;

import tools.mdsd.jamopp.model.java.commons.tests.CommonsTests;

import tools.mdsd.jamopp.model.java.containers.tests.ContainersTests;

import tools.mdsd.jamopp.model.java.expressions.tests.ExpressionsTests;

import tools.mdsd.jamopp.model.java.generics.tests.GenericsTests;

import tools.mdsd.jamopp.model.java.imports.tests.ImportsTests;

import tools.mdsd.jamopp.model.java.instantiations.tests.InstantiationsTests;

import tools.mdsd.jamopp.model.java.literals.tests.LiteralsTests;

import tools.mdsd.jamopp.model.java.members.tests.MembersTests;

import tools.mdsd.jamopp.model.java.modifiers.tests.ModifiersTests;

import tools.mdsd.jamopp.model.java.modules.tests.ModulesTests;

import tools.mdsd.jamopp.model.java.operators.tests.OperatorsTests;

import tools.mdsd.jamopp.model.java.parameters.tests.ParametersTests;

import tools.mdsd.jamopp.model.java.references.tests.ReferencesTests;

import tools.mdsd.jamopp.model.java.statements.tests.StatementsTests;

import tools.mdsd.jamopp.model.java.types.tests.TypesTests;

import tools.mdsd.jamopp.model.java.variables.tests.VariablesTests;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Java</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaAllTests extends TestSuite {

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
		TestSuite suite = new JavaAllTests("Java Tests");
		suite.addTest(AnnotationsTests.suite());
		suite.addTest(ArraysTests.suite());
		suite.addTest(ClassifiersTests.suite());
		suite.addTest(CommonsTests.suite());
		suite.addTest(ContainersTests.suite());
		suite.addTest(ExpressionsTests.suite());
		suite.addTest(GenericsTests.suite());
		suite.addTest(ImportsTests.suite());
		suite.addTest(InstantiationsTests.suite());
		suite.addTest(LiteralsTests.suite());
		suite.addTest(MembersTests.suite());
		suite.addTest(ModifiersTests.suite());
		suite.addTest(OperatorsTests.suite());
		suite.addTest(ParametersTests.suite());
		suite.addTest(ReferencesTests.suite());
		suite.addTest(StatementsTests.suite());
		suite.addTest(TypesTests.suite());
		suite.addTest(VariablesTests.suite());
		suite.addTest(ModulesTests.suite());
		return suite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaAllTests(String name) {
		super(name);
	}

} //JavaAllTests
