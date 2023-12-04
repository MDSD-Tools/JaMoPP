package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToAdditiveExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToEqualityExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToMultiplicativeExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToRelationExpressionConverter;
import jamopp.parser.jdt.converter.implementation.ToShiftExpressionConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerInfixExpression extends Handler {

	private final ExpressionsFactory expressionsFactory;
	private final ToEqualityExpressionConverter toEqualityExpressionConverter;
	private final ToRelationExpressionConverter toRelationExpressionConverter;
	private final ToShiftExpressionConverter toShiftExpressionConverter;
	private final ToAdditiveExpressionConverter toAdditiveExpressionConverter;
	private final ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;

	@Inject
	HandlerInfixExpression(UtilLayout utilLayout, ToShiftExpressionConverter toShiftExpressionConverter,
			ToRelationExpressionConverter toRelationExpressionConverter,
			ToMultiplicativeExpressionConverter toMultiplicativeExpressionConverter,
			ToExpressionConverter toExpressionConverter, ToEqualityExpressionConverter toEqualityExpressionConverter,
			ToAdditiveExpressionConverter toAdditiveExpressionConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.toEqualityExpressionConverter = toEqualityExpressionConverter;
		this.toRelationExpressionConverter = toRelationExpressionConverter;
		this.toShiftExpressionConverter = toShiftExpressionConverter;
		this.toAdditiveExpressionConverter = toAdditiveExpressionConverter;
		this.toMultiplicativeExpressionConverter = toMultiplicativeExpressionConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		InfixExpression infix = (InfixExpression) expr;
		if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
			org.emftext.language.java.expressions.ConditionalOrExpression result;
			org.emftext.language.java.expressions.Expression ex = toExpressionConverter
					.convert(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ConditionalOrExpression) {
				result = (org.emftext.language.java.expressions.ConditionalOrExpression) ex;
			} else {
				result = expressionsFactory.createConditionalOrExpression();
				result.getChildren().add((ConditionalOrExpressionChild) ex);
			}
			result.getChildren().add(
					(ConditionalOrExpressionChild) toExpressionConverter.convert(infix.getRightOperand()));
			infix.extendedOperands().forEach(obj -> result.getChildren()
					.add((ConditionalOrExpressionChild) toExpressionConverter.convert((Expression) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
			org.emftext.language.java.expressions.ConditionalAndExpression result;
			org.emftext.language.java.expressions.Expression ex = toExpressionConverter
					.convert(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ConditionalAndExpression) {
				result = (org.emftext.language.java.expressions.ConditionalAndExpression) ex;
			} else {
				result = expressionsFactory.createConditionalAndExpression();
				result.getChildren().add((ConditionalAndExpressionChild) ex);
			}
			result.getChildren().add(
					(ConditionalAndExpressionChild) toExpressionConverter.convert(infix.getRightOperand()));
			infix.extendedOperands().forEach(obj -> result.getChildren()
					.add((ConditionalAndExpressionChild) toExpressionConverter.convert((Expression) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.OR) {
			org.emftext.language.java.expressions.InclusiveOrExpression result;
			org.emftext.language.java.expressions.Expression ex = toExpressionConverter
					.convert(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.InclusiveOrExpression) {
				result = (org.emftext.language.java.expressions.InclusiveOrExpression) ex;
			} else {
				result = expressionsFactory.createInclusiveOrExpression();
				result.getChildren().add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) ex);
			}
			result.getChildren()
					.add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) toExpressionConverter
							.convert(infix.getRightOperand()));
			infix.extendedOperands()
					.forEach(obj -> result.getChildren().add(
							(org.emftext.language.java.expressions.InclusiveOrExpressionChild) toExpressionConverter
									.convert((Expression) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.XOR) {
			org.emftext.language.java.expressions.ExclusiveOrExpression result;
			org.emftext.language.java.expressions.Expression ex = toExpressionConverter
					.convert(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.ExclusiveOrExpression) {
				result = (org.emftext.language.java.expressions.ExclusiveOrExpression) ex;
			} else {
				result = expressionsFactory.createExclusiveOrExpression();
				result.getChildren().add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) ex);
			}
			result.getChildren()
					.add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) toExpressionConverter
							.convert(infix.getRightOperand()));
			infix.extendedOperands()
					.forEach(obj -> result.getChildren().add(
							(org.emftext.language.java.expressions.ExclusiveOrExpressionChild) toExpressionConverter
									.convert((Expression) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.AND) {
			org.emftext.language.java.expressions.AndExpression result;
			org.emftext.language.java.expressions.Expression ex = toExpressionConverter
					.convert(infix.getLeftOperand());
			if (ex instanceof org.emftext.language.java.expressions.AndExpression) {
				result = (org.emftext.language.java.expressions.AndExpression) ex;
			} else {
				result = expressionsFactory.createAndExpression();
				result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) ex);
			}
			result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) toExpressionConverter
					.convert(infix.getRightOperand()));
			infix.extendedOperands()
					.forEach(obj -> result.getChildren()
							.add((org.emftext.language.java.expressions.AndExpressionChild) toExpressionConverter
									.convert((Expression) obj)));
			utilLayout.convertToMinimalLayoutInformation(result, infix);
			return result;
		} else if (infix.getOperator() == InfixExpression.Operator.EQUALS
				|| infix.getOperator() == InfixExpression.Operator.NOT_EQUALS) {
			return toEqualityExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.GREATER
				|| infix.getOperator() == InfixExpression.Operator.GREATER_EQUALS
				|| infix.getOperator() == InfixExpression.Operator.LESS
				|| infix.getOperator() == InfixExpression.Operator.LESS_EQUALS) {
			return toRelationExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.LEFT_SHIFT
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_SIGNED
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
			return toShiftExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.PLUS
				|| infix.getOperator() == InfixExpression.Operator.MINUS) {
			return toAdditiveExpressionConverter.convertToAdditiveExpression(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.TIMES
				|| infix.getOperator() == InfixExpression.Operator.DIVIDE
				|| infix.getOperator() == InfixExpression.Operator.REMAINDER) {
			return toMultiplicativeExpressionConverter.convert(infix);
		} else {
			return null;
		}
	}

}
