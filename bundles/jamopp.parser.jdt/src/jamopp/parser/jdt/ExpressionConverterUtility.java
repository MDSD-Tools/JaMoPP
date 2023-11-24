/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.OperatorsFactory;

class ExpressionConverterUtility {

	private static final ToExpressionConverter toExpressionConverter;
	private static final ToConditionalExpressionConverter toConditionalExpressionConverter;

	static {

		OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;
		ExpressionsFactory expressionsFactory = ExpressionsFactory.eINSTANCE;
		
		LayoutInformationConverter layoutInformationConverter = new LayoutInformationConverter();

		ToAssignmentConverter toAssignmentOperatorConverter = new ToAssignmentConverter();
		ToEqualityOperatorConverter toEqualityOperatorConverter = new ToEqualityOperatorConverter();
		ToRelationOperatorConverter toRelationOperatorConverter = new ToRelationOperatorConverter();
		ToShiftOperatorConverter toShiftOperatorConverter = new ToShiftOperatorConverter(operatorsFactory);
		ToAdditiveOperatorConverter toAdditiveOperatorConverter = new ToAdditiveOperatorConverter();
		ToUnaryOperatorConverter toUnaryOperatorConverter = new ToUnaryOperatorConverter();
		ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter = new ToMultiplicativeOperatorConverter();

		toExpressionConverter = new ToExpressionConverter();
		toConditionalExpressionConverter = new ToConditionalExpressionConverter(toExpressionConverter);

		ToPrimaryExpressionConverter toPrimaryExpressionConverter = new ToPrimaryExpressionConverter();
		ToEqualityExpressionConverter toEqualityExpressionConverter = new ToEqualityExpressionConverter(
				toExpressionConverter, toEqualityOperatorConverter);
		ToRelationExpressionConverter toRelationExpressionConverter = new ToRelationExpressionConverter(
				toRelationOperatorConverter, toExpressionConverter);
		ToShiftExpressionConverter toShiftExpressionConverter = new ToShiftExpressionConverter(toShiftOperatorConverter,
				toExpressionConverter);
		ToAdditiveExpressionConverter toAdditiveExpressionConverter = new ToAdditiveExpressionConverter(
				toExpressionConverter, toAdditiveOperatorConverter);
		ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter = new ToMultiplicativeExpressionConverter(
				toMultiplicativeOperatorConverter, toExpressionConverter);
		ToUnaryExpressionConverter toUnaryExpressionConverter = new ToUnaryExpressionConverter(toUnaryOperatorConverter,
				toExpressionConverter, layoutInformationConverter, expressionsFactory);
		ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter = new ToMethodReferenceExpressionConverter(
				toExpressionConverter);

		toExpressionConverter.setToAssignmentOperatorConverter(toAssignmentOperatorConverter);
		toExpressionConverter.setToConditionalExpressionConverter(toConditionalExpressionConverter);
		toExpressionConverter.setToEqualityExpressionConverter(toEqualityExpressionConverter);
		toExpressionConverter.setToRelationExpressionConverter(toRelationExpressionConverter);
		toExpressionConverter.setToShiftExpressionConverter(toShiftExpressionConverter);
		toExpressionConverter.setToAdditiveExpressionConverter(toAdditiveExpressionConverter);
		toExpressionConverter.setToMultiplicativeExpressionConverter(toMultiplicativeExpressionConverter);
		toExpressionConverter.setToUnaryExpressionConverter(toUnaryExpressionConverter);
		toExpressionConverter.setToMethodReferenceExpressionConverter(toMethodReferenceExpressionConverter);
		toExpressionConverter.setToPrimaryExpressionConverter(toPrimaryExpressionConverter);

	}

	org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		return toExpressionConverter.convertToExpression(expr);
	}

	org.emftext.language.java.expressions.ConditionalExpression convertToConditionalExpression(
			ConditionalExpression expr) {
		return toConditionalExpressionConverter.convertToConditionalExpression(expr);
	}
}
