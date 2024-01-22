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
package tools.mdsd.jamopp.model.java.extensions.expressions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import tools.mdsd.jamopp.model.java.arrays.ArrayInstantiationBySize;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AndExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;

public final class ExpressionExtension {

	private static final String CLONE = "clone";

	private ExpressionExtension() {
		// Should not be initiated.
	}

	/**
	 * Returns the type of the expression considering all concrete subtypes of the
	 * Expression.
	 *
	 * @return type of expression
	 */
	public static Type getType(Expression expression) {
		return expression.getOneType(false);
	}

	public static Type getAlternativeType(Expression expression) {
		return expression.getOneType(true);
	}

	public static Type getOneType(Expression expression, boolean alternative) {
		tools.mdsd.jamopp.model.java.classifiers.Class stringClass = expression.getStringClass();

		Type type = null;

		if (expression instanceof Reference reference) {
			// navigate down references
			while (reference.getNext() != null) {
				reference = reference.getNext();
			}
			type = reference.getReferencedType();
		} else if (expression instanceof Literal) {
			type = ((Literal) expression).getType();
		} else if (expression instanceof CastExpression) {
			type = ((CastExpression) expression).getTypeReference().getTarget();
		} else if (expression instanceof AssignmentExpression) {
			type = ((AssignmentExpression) expression).getChild().getOneType(alternative);
		} else if (expression instanceof ConditionalExpression
				&& ((ConditionalExpression) expression).getExpressionIf() != null) {
			if (alternative) {
				type = ((ConditionalExpression) expression).getExpressionElse().getOneType(alternative);
			} else {
				type = ((ConditionalExpression) expression).getExpressionIf().getOneType(alternative);
			}

		} else if (expression instanceof EqualityExpression || expression instanceof RelationExpression
				|| expression instanceof ConditionalOrExpression || expression instanceof ConditionalAndExpression
				|| expression instanceof InstanceOfExpression) {
			type = expression.getLibClass("Boolean");
		} else if (expression instanceof AdditiveExpression || expression instanceof MultiplicativeExpression
				|| expression instanceof InclusiveOrExpression || expression instanceof ExclusiveOrExpression
				|| expression instanceof AndExpression || expression instanceof ShiftExpression) {

			if (expression instanceof AdditiveExpression additiveExpression) {
				for (Expression subExp : additiveExpression.getChildren()) {
					if (stringClass.equals(subExp.getOneType(alternative))) {
						// special case: string concatenation
						return stringClass;
					}
				}
			}

			@SuppressWarnings("unchecked")
			Expression subExp = ((EList<Expression>) expression
					.eGet(expression.eClass().getEStructuralFeature("children"))).get(0);

			return subExp.getOneType(alternative);
		} else if (expression instanceof UnaryExpression) {
			Expression subExp = ((UnaryExpression) expression).getChild();
			return subExp.getOneType(alternative);
		} else {
			for (TreeIterator<EObject> i = expression.eAllContents(); i.hasNext();) {
				EObject next = i.next();
				Type nextType = null;

				if (next instanceof PrimaryExpression) {

					if (next instanceof Reference ref) {
						// navigate down references
						while (ref.getNext() != null) {
							ref = ref.getNext();
						}
						next = ref;
					}
					if (next instanceof Literal) {
						nextType = ((Literal) next).getType();
					} else if (next instanceof CastExpression) {
						nextType = ((CastExpression) next).getTypeReference().getTarget();
					} else {
						nextType = ((Reference) next).getReferencedType();
					}
					i.prune();

				}
				if (nextType != null) {
					type = nextType;
					// in the special case that this is an expression with
					// some string included, everything is converted to string
					if (stringClass.equals(type)) {
						break;
					}
				}
			}
		}
		// type can be null in cases of unresolved/unresolvable proxies
		return type;
	}

	public static long getArrayDimension(Expression expression) {
		long result;
		if (expression instanceof NestedExpression && ((NestedExpression) expression).getNext() == null) {
			result = ((NestedExpression) expression).getExpression().getArrayDimension()
					- ((NestedExpression) expression).getArraySelectors().size();
		} else if (expression instanceof ConditionalExpression
				&& ((ConditionalExpression) expression).getExpressionIf() != null) {
			result = ((ConditionalExpression) expression).getExpressionIf().getArrayDimension();
		} else if (expression instanceof AssignmentExpression) {
			Expression value = ((AssignmentExpression) expression).getValue();
			if (value == null) {
				result = 0;
			} else {
				result = value.getArrayDimension();
			}
		} else if (expression instanceof InstanceOfExpression) {
			result = 0;
		} else {
			result = calcSize(expression);
		}
		return result;
	}

	private static long calcSize(Expression expression) {
		long size = 0;
		ArrayTypeable arrayType = null;
		if (expression instanceof Reference reference) {
			while (reference.getNext() != null) {
				reference = reference.getNext();
			}
			// an array clone? -> dimension defined by cloned array
			if (reference instanceof ElementReference && reference.getPrevious() != null) {
				ReferenceableElement target = ((ElementReference) reference).getTarget();
				if (target instanceof Method && CLONE.equals(((Method) target).getName())) {
					reference = (Reference) reference.eContainer();
				}
			}
			if (reference instanceof ElementReference elementReference) {
				if (elementReference.getTarget() instanceof ArrayTypeable) {
					arrayType = (ArrayTypeable) elementReference.getTarget();
				}
				if (elementReference.getTarget() instanceof AdditionalLocalVariable) {
					AdditionalLocalVariable additionalLocalVariable = (AdditionalLocalVariable) elementReference
							.getTarget();
					arrayType = (LocalVariable) additionalLocalVariable.eContainer();
					size += additionalLocalVariable.getArrayDimensionsAfter().size();
					size -= arrayType.getArrayDimensionsAfter().size();
				}
				if (elementReference.getTarget() instanceof AdditionalField) {
					AdditionalField additionalField = (AdditionalField) elementReference.getTarget();
					arrayType = (Field) additionalField.eContainer();
					size += additionalField.getArrayDimensionsAfter().size();
					size -= arrayType.getArrayDimensionsAfter().size();
				}
			} else if (expression instanceof ArrayTypeable) {
				size += ((ArrayTypeable) expression).getArrayDimensionsBefore().size()
						+ ((ArrayTypeable) expression).getArrayDimensionsAfter().size();
				if (expression instanceof VariableLengthParameter) {
					size++;
				}
			}
			size -= reference.getArraySelectors().size();
		} else if (expression instanceof ArrayTypeable) {
			size += ((ArrayTypeable) expression).getArrayDimensionsBefore().size()
					+ ((ArrayTypeable) expression).getArrayDimensionsAfter().size();
			if (expression instanceof VariableLengthParameter) {
				size++;
			}
		}

		if (expression instanceof ArrayInstantiationBySize) {
			size += ((ArrayInstantiationBySize) expression).getSizes().size();
		}

		if (arrayType != null) {
			size += arrayType.getArrayDimension();
		}

		return size;
	}
}
