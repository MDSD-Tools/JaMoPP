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
 *   Martin Armbruster
 *      - Extension of getStatements()
 ******************************************************************************/
package org.emftext.language.java.extensions.members;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.references.MethodCall;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.types.Type;
import org.emftext.language.java.types.TypeReference;

public class MethodExtension {

	/**
	 * Returns <code>true</code> if the given method matches the given call.
	 *
	 * @param methodCall
	 * @return
	 */
	public static boolean isSomeMethodForCall(Method me, MethodCall methodCall) {
		return me.isMethodForCall(methodCall, false);
	}

	/**
	 * Returns <code>true</code> if the given {@link Method} <code>me</code> is
	 * a better match for the given method call than {@link Method}
	 * <code>otherMethod</code>.
	 *
	 * @param otherMethod
	 * @param methodCall
	 * @return
	 */
	public static boolean isBetterMethodForCall(Method me, Method otherMethod,
			MethodCall methodCall) {

		if (!me.isMethodForCall(methodCall, false)) {
			return false;
		}

		if (otherMethod.isMethodForCall(methodCall, true)) {
			if (me.isMethodForCall(methodCall, true)) {
				// We both match perfectly; lets compare our return types
				Type target = me.getTypeReference().getTarget();
				if ((target instanceof ConcreteClassifier) && ((ConcreteClassifier) target).getAllSuperClassifiers().contains(
						otherMethod.getTypeReference().getTarget())) {
					// I am the more concrete type
					return true;
				}
			}

			//the other already matches perfectly; I am not better
			return false;
		}

		if (!otherMethod.isMethodForCall(methodCall, false)) {
			//I match, but the other does not
			return true;
		}
		//we both match, I am only better if I match perfectly <-
		//TODO #763: this is not enough: we need to check for "nearest subtype" here
		return me.isMethodForCall(methodCall, true);
	}

	public static boolean isMethodForCall(Method me, MethodCall methodCall,
			boolean needsPerfectMatch) {

		EList<Type> argumentTypeList = methodCall.getArgumentTypes();
		EList<Parameter> parameterList = new BasicEList<>(me.getParameters());

		EList<Type> parameterTypeList = new BasicEList<>();
		for (Parameter parameter : parameterList)  {
			// Determine types before messing with the parameters
			TypeReference typeReference = parameter.getTypeReference();
			Type boundTarget = typeReference.getBoundTarget(methodCall);
			parameterTypeList.add(boundTarget);
		}

		if (!parameterList.isEmpty()) {
			Parameter lastParameter = parameterList.get(parameterList.size() - 1);
			Type lastParameterType  = parameterTypeList.get(parameterTypeList.size() - 1);
			if (lastParameter instanceof VariableLengthParameter) {
				// In case of variable length add/remove some parameters
				while (parameterList.size() < argumentTypeList.size()) {
					if (needsPerfectMatch) {
						return false;
					}
					parameterList.add(lastParameter);
					parameterTypeList.add(lastParameterType);
				}

				if (parameterList.size() > argumentTypeList.size()) {
					if (needsPerfectMatch) {
						return false;
					}
					parameterList.remove(lastParameter);
					parameterTypeList.remove(parameterTypeList.size() - 1);
				}
			}
		}

		// TODO Perform early exit instead
		if (parameterList.size() == argumentTypeList.size()) {
			boolean parametersMatch = true;
			for (int i = 0; i < argumentTypeList.size(); i++) {
				Type parameterType = parameterTypeList.get(i);
				Type argumentType = argumentTypeList.get(i);

				if (argumentType == null || parameterType == null || (parameterType.eIsProxy() && argumentType.eIsProxy())) {
					return false;
				}
				Expression argument = methodCall.getArguments().get(i);
				long argumentArrayDimension = argument.getArrayDimension();
				Parameter parameter = parameterList.get(i);
				if (needsPerfectMatch) {
					long parameterArrayDimension = parameter.getArrayDimension();
					parametersMatch = parametersMatch
							&& argumentType.equalsType(argumentArrayDimension,
									parameterType, parameterArrayDimension);
				} else {
					parametersMatch = parametersMatch
							&& argumentType.isSuperType(argumentArrayDimension,
									parameterType, parameter);
				}

				// TODO Return if parametersMatch is 'false'? There is not need
				// to check the other parameters because once parametersMatch is
				// 'false' it wont become true anymore.
			}
			return parametersMatch;
		}

		return false;
	}

	/**
	 * Returns a list of all statements within the block of a method.
	 * This is a legacy method to provide a stable and backwards-compatible API.
	 *
	 * @param me the method for which the statements are obtained.
	 * @return the list of all statements.
	 * @deprecated Use getBlock().getStatements().
	 */
	@Deprecated
	public static EList<Statement> getStatements(Method me) {
		Block b = getBlock(me);
		return b != null ? b.getStatements() : new BasicEList<>();
	}

	/**
	 * Returns a block representing the body of a method.
	 *
	 * @param me the method for which the body is returned.
	 * @return the block or null if the method has no implementation.
	 */
	public static Block getBlock(Method me) {
		if (me.getStatement() instanceof Block) {
			return (Block) me.getStatement();
		}
		return null;
	}
}
