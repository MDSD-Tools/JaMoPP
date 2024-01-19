/*******************************************************************************
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
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.extensions.variables;

import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.ReferencesFactory;
import tools.mdsd.jamopp.model.java.statements.ExpressionStatement;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.variables.Variable;

public final class VariableExtension {

	private VariableExtension() {
		// Should not initiated.
	}

	/**
	 * Creates a statement that calls the method with the given name on this
	 * variable. If the variable's type does not offer such a method, null is
	 * returned.
	 *
	 * @param methodName
	 * @param arguments
	 */
	public static ExpressionStatement createMethodCallStatement(Variable variable, String methodName,
			EList<Expression> arguments) {

		ExpressionStatement callStatement = StatementsFactory.eINSTANCE.createExpressionStatement();
		callStatement.setExpression(variable.createMethodCall(methodName, arguments));
		return callStatement;
	}

	/**
	 * Creates an expression that calls the method with the given name on this
	 * variable. If the variable's type does not offer such a method, null is
	 * returned.
	 *
	 * @param methodName
	 * @param arguments
	 */
	public static IdentifierReference createMethodCall(Variable variable, String methodName,
			EList<Expression> arguments) {
		IdentifierReference thisRef = ReferencesFactory.eINSTANCE.createIdentifierReference();
		thisRef.setTarget(variable);
		IdentifierReference result = null;
		Type thisType = variable.getTypeReference().getTarget();
		if (thisType instanceof MemberContainer castedType && castedType.getContainedMethod(methodName) != null) {
			MethodCall methodCall = ReferencesFactory.eINSTANCE.createMethodCall();
			methodCall.setTarget(castedType.getContainedMethod(methodName));
			// add arguments
			methodCall.getArguments().addAll(arguments);
			thisRef.setNext(methodCall);
			result = thisRef;
		}
		return result;
	}
}
