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
package tools.mdsd.jamopp.model.java.extensions.members;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;

public final class MethodExtension {

	private MethodExtension() {
		// Should not be initiated.
	}

	/**
	 * Returns <code>true</code> if the given method matches the given call.
	 *
	 * @param methodCall
	 * @return
	 */
	public static boolean isSomeMethodForCall(final Method method, final MethodCall methodCall) {
		return method.isMethodForCall(methodCall, false);
	}

	/**
	 * Returns <code>true</code> if the given {@link Method} <code>me</code> is a
	 * better match for the given method call than {@link Method}
	 * <code>otherMethod</code>.
	 *
	 * @param otherMethod
	 * @param methodCall
	 * @return
	 */
	public static boolean isBetterMethodForCall(final Method method, final Method otherMethod,
			final MethodCall methodCall) {
		boolean result;
		if (method.isMethodForCall(methodCall, false)) {
			if (otherMethod.isMethodForCall(methodCall, true) && method.isMethodForCall(methodCall, true)) {
				// We both match perfectly; lets compare our return types
				final Type target = method.getTypeReference().getTarget();
				if (target instanceof ConcreteClassifier && ((ConcreteClassifier) target).getAllSuperClassifiers()
						.contains(otherMethod.getTypeReference().getTarget())) {
					// I am the more concrete type
					result = true;
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} else if (otherMethod.isMethodForCall(methodCall, false)) {
			// we both match, I am only better if I match perfectly
			result = method.isMethodForCall(methodCall, true);
		} else {
			// I match, but the other does not
			result = true;
		}

		return result;
	}

	public static boolean isMethodForCall(final Method method, final MethodCall methodCall,
			final boolean needsPerfectMatch) {

		final EList<Type> argumentTypeList = methodCall.getArgumentTypes();
		final EList<Parameter> parameterList = new BasicEList<>(method.getParameters());

		final EList<Type> parameterTypeList = new BasicEList<>();
		for (final Parameter parameter : parameterList) {
			// Determine types before messing with the parameters
			final TypeReference typeReference = parameter.getTypeReference();
			final Type boundTarget = typeReference.getBoundTarget(methodCall);
			parameterTypeList.add(boundTarget);
		}

		if (!parameterList.isEmpty()) {
			final Parameter lastParameter = parameterList.get(parameterList.size() - 1);
			final Type lastParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
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

		if (parameterList.size() == argumentTypeList.size()) {
			boolean parametersMatch = true;
			for (int i = 0; i < argumentTypeList.size(); i++) {
				final Type parameterType = parameterTypeList.get(i);
				final Type argumentType = argumentTypeList.get(i);

				if (argumentType == null || parameterType == null
						|| parameterType.eIsProxy() && argumentType.eIsProxy()) {
					return false;
				}
				final Expression argument = methodCall.getArguments().get(i);
				final long argumentArrayDimension = argument.getArrayDimension();
				final Parameter parameter = parameterList.get(i);
				if (needsPerfectMatch) {
					final long parameterArrayDimension = parameter.getArrayDimension();
					parametersMatch = parametersMatch
							&& argumentType.equalsType(argumentArrayDimension, parameterType, parameterArrayDimension);
				} else {
					parametersMatch = parametersMatch
							&& argumentType.isSuperType(argumentArrayDimension, parameterType, parameter);
				}

			}
			return parametersMatch;
		}

		return false;
	}

	/**
	 * Returns a block representing the body of a method.
	 *
	 * @param method the method for which the body is returned.
	 * @return the block or null if the method has no implementation.
	 */
	public static Block getBlock(final Method method) {
		Block result = null;
		if (method.getStatement() instanceof final Block block) {
			result = block;
		}
		return result;
	}
}
