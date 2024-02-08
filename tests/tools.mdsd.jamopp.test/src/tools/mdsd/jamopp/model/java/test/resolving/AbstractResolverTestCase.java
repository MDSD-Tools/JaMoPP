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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.test.AbstractJaMoPPTests;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;

/**
 * An abstract super class for all test cases that check reference resolving. It provides some
 * assert methods that can be used to check the correctness of reference targets.
 */
public abstract class AbstractResolverTestCase extends AbstractJaMoPPTests {

    protected static final String TEST_INPUT_FOLDER_RESOLVING = "src-input" + File.separator + "resolving"
            + File.separator;

    protected void assertIsCallToMethod(Statement statement, Method expectedCallTarget) {
        assertType(statement, ExpressionStatement.class);
        final ExpressionStatement expression = (ExpressionStatement) statement;
        final Expression methodCallExpression = expression.getExpression();
        assertNotNull(methodCallExpression);
        assertType(methodCallExpression, MethodCall.class);
        final MethodCall mc = (MethodCall) methodCallExpression;
        assertEquals(expectedCallTarget, mc.getTarget());
    }

    protected Field assertIsField(Member member, String expectedName) {
        assertType(member, Field.class);
        final Field field = (Field) member;
        assertEquals(expectedName, field.getName());
        return field;
    }

    protected ClassMethod assertIsMethod(Member member, String expectedName) {
        assertType(member, ClassMethod.class);
        final ClassMethod method = (ClassMethod) member;
        assertEquals(expectedName, method.getName());
        return method;
    }

    protected void assertIsReferenceToField(Statement statement, Field expectedReferenceTarget) {
        assertType(statement, ExpressionStatement.class);
        final ExpressionStatement expression = (ExpressionStatement) statement;
        final Expression expr = expression.getExpression();
        assertNotNull(expr);
        assertType(expr, AssignmentExpression.class);
        final Expression expr2 = ((AssignmentExpression) expr).getChild();
        assertNotNull(expr2);
        assertType(expr2, IdentifierReference.class);
        final IdentifierReference identifierReference = (IdentifierReference) expr2;
        assertEquals(expectedReferenceTarget, identifierReference.getTarget());
    }

    protected void assertIsReferenceToLocalVariable(Statement statement, LocalVariable expectedReferenceTarget) {
        assertType(statement, ExpressionStatement.class);
        final ExpressionStatement expression = (ExpressionStatement) statement;
        final Expression expr = expression.getExpression();
        assertNotNull(expr);
        assertType(expr, AssignmentExpression.class);
        final Expression expr2 = ((AssignmentExpression) expr).getChild();
        assertNotNull(expr2);
        assertType(expr2, IdentifierReference.class);
        final IdentifierReference identifierReference = (IdentifierReference) expr2;
        assertEquals(expectedReferenceTarget, identifierReference.getTarget());
    }

    @Override
    protected String getTestInputFolder() {
        return TEST_INPUT_FOLDER_RESOLVING;
    }

    @Override
    protected boolean ignoreSemanticErrors(String filename) {
        return false;
    }

    @Override
    protected boolean isExcludedFromReprintTest(String filename) {
        return true;
    }
}
