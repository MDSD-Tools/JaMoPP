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

class ExpressionConverterUtility {

	private static final ToAssignmentConverter toAssignmentOperatorConverter;
	private static final ToEqualityOperatorConverter toEqualityOperatorConverter;
	private static final ToRelationOperatorConverter toRelationOperatorConverter;
	private static final ToShiftOperatorConverter toShiftOperatorConverter;
	private static final ToAdditiveOperatorConverter toAdditiveOperatorConverter;
	private static final ToUnaryOperatorConverter toUnaryOperatorConverter;
	private static final ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter;

	private static final ToExpressionConverter toExpressionConverter;
	private static final ToPrimaryExpressionConverter toPrimaryExpressionConverter;
	private static final ToConditionalExpressionConverter toConditionalExpressionConverter;
	private static final ToEqualityExpressionConverter toEqualityExpressionConverter;
	private static final ToRelationExpressionConverter toRelationExpressionConverter;
	private static final ToShiftExpressionConverter toShiftExpressionConverter;
	private static final ToAdditiveExpressionConverter toAdditiveExpressionConverter;
	private static final ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter;
	private static final ToUnaryExpressionConverter toUnaryExpressionConverter;
	private static final ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;

	static {

		toAssignmentOperatorConverter = new ToAssignmentConverter();
		toEqualityOperatorConverter = new ToEqualityOperatorConverter();
		toRelationOperatorConverter = new ToRelationOperatorConverter();
		toShiftOperatorConverter = new ToShiftOperatorConverter();
		toAdditiveOperatorConverter = new ToAdditiveOperatorConverter();
		toUnaryOperatorConverter = new ToUnaryOperatorConverter();
		toMultiplicativeOperatorConverter = new ToMultiplicativeOperatorConverter();

		toExpressionConverter = new ToExpressionConverter();
		toPrimaryExpressionConverter = new ToPrimaryExpressionConverter();
		toConditionalExpressionConverter = new ToConditionalExpressionConverter(toExpressionConverter);
		toEqualityExpressionConverter = new ToEqualityExpressionConverter(toExpressionConverter,
				toEqualityOperatorConverter);
		toRelationExpressionConverter = new ToRelationExpressionConverter(toRelationOperatorConverter,
				toExpressionConverter);
		toShiftExpressionConverter = new ToShiftExpressionConverter(toShiftOperatorConverter, toExpressionConverter);
		toAdditiveExpressionConverter = new ToAdditiveExpressionConverter(toExpressionConverter,
				toAdditiveOperatorConverter);
		toMultiplicativeExpressionConverter = new ToMultiplicativeExpressionConverter(toMultiplicativeOperatorConverter,
				toExpressionConverter);
		toUnaryExpressionConverter = new ToUnaryExpressionConverter(toUnaryOperatorConverter, toExpressionConverter);
		toMethodReferenceExpressionConverter = new ToMethodReferenceExpressionConverter(toExpressionConverter);

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

	@SuppressWarnings("unchecked")
	static org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		return toExpressionConverter.convertToExpression(expr);
	}

	static org.emftext.language.java.expressions.ConditionalExpression convertToConditionalExpression(
			ConditionalExpression expr) {
		return toConditionalExpressionConverter.convertToConditionalExpression(expr);
	}
}
