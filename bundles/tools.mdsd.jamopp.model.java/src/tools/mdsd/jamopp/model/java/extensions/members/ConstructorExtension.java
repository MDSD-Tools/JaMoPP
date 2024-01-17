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
 *   Benjamin Klatt
 *      - initial API and implementation
 *   Martin Armbruster
 *      - Extension with getStatements()
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.extensions.members;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.parameters.Parameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;

/**
 * Extension providing utility methods for the the Constructor meta model class.
 */

public class ConstructorExtension {

	/**
	 * Returns <code>true</code> if the given {@link Constructor} <code>co</code> is
	 * a better match for the given constructor call than {@link Constructor}
	 * <code>other</code>.
	 *
	 * Possible Result:
	 * <ol>
	 * <li><code>other</code> is perfect match: <code>co</code> can not be
	 * better</li>
	 * <li><code>other</code> is weak match: true only if <code>co</code> is perfect
	 * match</li>
	 * <li><code>other</code> does not match: True only if <code>co</code> is at
	 * least weak match</li>
	 * </ol>
	 *
	 * @param co    The constructor to check if it is better.
	 * @param other The existing constructor to compare with.
	 * @param call  The call to check for.
	 * @return True only if the new {@link Constructor} <code>co</code> is better
	 *         than the other one.
	 */
	public static boolean isBetterConstructorForCall(Constructor co, Constructor other, NewConstructorCall call) {

		if (isConstructorForCall(other, call, true)) {
			return false;
		}

		if (isConstructorForCall(other, call, false)) {
			return isConstructorForCall(co, call, true);
		}

		return isConstructorForCall(co, call, false);
	}

	/**
	 * Check if a constructor element is valid for a constructor call.
	 *
	 * Note, this method has been originally copied from the MethodExtension class
	 * as the behavior is nearly the same, but Constructor and Method have no common
	 * super type. The only difference in the implementation is the check of the
	 * return type. Constructors must return the exact type of the classifier they
	 * are member of. General methods can have an arbitrary return type. The latter
	 * is checked by the MethodExtension class as part of the "weak match" check in
	 * the isBetterMethodForCall check. For the constructor, the return type is a
	 * hard criteria and thus checked as part of the general
	 * {@link #isConstructorForCall(Constructor, NewConstructorCall, boolean)}.
	 *
	 * @param co                The constructor to check.
	 * @param call              The call to check for.
	 * @param needsPerfectMatch Flag how to handle parameters with variable argument
	 *                          (array) length
	 * @return True if the constructor is valid for the call.
	 */
	public static boolean isConstructorForCall(Constructor co, NewConstructorCall call, boolean needsPerfectMatch) {

		Type callType = call.getReferencedType();
		if (!(callType instanceof ConcreteClassifier) || !((ConcreteClassifier) callType).getMembers().contains(co)) {
			return false;
		}

		EList<Type> argumentTypeList = call.getArgumentTypes();
		EList<Parameter> parameterList = new BasicEList<>(co.getParameters());

		EList<Type> parameterTypeList = new BasicEList<>();
		for (Parameter parameter : parameterList) {
			// Determine types before messing with the parameters
			TypeReference typeReference = parameter.getTypeReference();
			Type boundTarget = typeReference.getBoundTarget(call);
			parameterTypeList.add(boundTarget);
		}

		if (!parameterList.isEmpty()) {
			Parameter lastParameter = parameterList.get(parameterList.size() - 1);
			Type lastParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
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
				Type parameterType = parameterTypeList.get(i);
				Type argumentType = argumentTypeList.get(i);

				if (argumentType == null || parameterType == null
						|| parameterType.eIsProxy() && argumentType.eIsProxy()) {
					return false;
				}
				Expression argument = call.getArguments().get(i);
				long argumentArrayDimension = argument.getArrayDimension();
				Parameter parameter = parameterList.get(i);
				if (needsPerfectMatch) {
					long parameterArrayDimension = parameter.getArrayDimension();
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
}
