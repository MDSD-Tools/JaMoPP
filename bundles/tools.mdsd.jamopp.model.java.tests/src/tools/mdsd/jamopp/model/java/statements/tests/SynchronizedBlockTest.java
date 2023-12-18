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
package tools.mdsd.jamopp.model.java.statements.tests;

import junit.textui.TestRunner;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.statements.SynchronizedBlock;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Synchronized Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link tools.mdsd.jamopp.model.java.statements.SynchronizedBlock#getStatements() <em>Get Statements</em>}</li>
 *   <li>{@link tools.mdsd.jamopp.model.java.statements.StatementListContainer#getLocalVariable(java.lang.String) <em>Get Local Variable</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class SynchronizedBlockTest extends StatementTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(SynchronizedBlockTest.class);
	}

	/**
	 * Constructs a new Synchronized Block test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SynchronizedBlockTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Synchronized Block test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected SynchronizedBlock getFixture() {
		return (SynchronizedBlock)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(StatementsFactory.eINSTANCE.createSynchronizedBlock());
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
	 * Tests the '{@link tools.mdsd.jamopp.model.java.statements.SynchronizedBlock#getStatements() <em>Get Statements</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.mdsd.jamopp.model.java.statements.SynchronizedBlock#getStatements()
	 * @generated
	 */
	public void testGetStatements() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

	/**
	 * Tests the '{@link tools.mdsd.jamopp.model.java.statements.StatementListContainer#getLocalVariable(java.lang.String) <em>Get Local Variable</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see tools.mdsd.jamopp.model.java.statements.StatementListContainer#getLocalVariable(java.lang.String)
	 * @generated
	 */
	public void testGetLocalVariable__String() {
		// TODO: implement this operation test method
		// Ensure that you remove @generated or mark it @generated NOT
		fail();
	}

} //SynchronizedBlockTest
