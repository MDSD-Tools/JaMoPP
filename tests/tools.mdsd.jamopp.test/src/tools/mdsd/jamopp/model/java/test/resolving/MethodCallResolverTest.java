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
package tools.mdsd.jamopp.test.resolving;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.statements.Statement;

/**
 * A test for resolving method calls to the respective method.
 */
public class MethodCallResolverTest extends AbstractResolverTestCase {

	@Test
	public void testReferencing() throws Exception {
		final String typename = "MethodCalls";
		final tools.mdsd.jamopp.model.java.classifiers.Class clazz = assertParsesToClass(typename);
		assertNotNull(clazz);
		assertMemberCount(clazz, 4);

		final List<Member> members = clazz.getMembers();

		final ClassMethod method1 = assertIsMethod(members.get(0), "m1");
		final ClassMethod method2 = assertIsMethod(members.get(1), "m2");
		final ClassMethod method3 = assertIsMethod(members.get(2), "m3");
		final ClassMethod method4 = assertIsMethod(members.get(3), "m3");

		final List<? extends Statement> methodStatements2 = method2.getStatements();

		assertIsCallToMethod(methodStatements2.get(0), method1);
		assertIsCallToMethod(methodStatements2.get(1), method2);
		assertIsCallToMethod(methodStatements2.get(2), method3);
		assertIsCallToMethod(methodStatements2.get(3), method4);
	}
}
