package tools.mdsd.jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.expressions.ShiftExpression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.handler.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerInfixExpression implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<InfixExpression, EqualityExpression> toEqualityExpressionConverter;
	private final Converter<InfixExpression, RelationExpression> toRelationExpressionConverter;
	private final Converter<InfixExpression, ShiftExpression> toShiftExpressionConverter;
	private final Converter<InfixExpression, AdditiveExpression> toAdditiveExpressionConverter;
	private final Converter<InfixExpression, MultiplicativeExpression> toMultiplicativeExpressionConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerInfixExpression(UtilLayout utilLayout,
			Converter<InfixExpression, ShiftExpression> toShiftExpressionConverter,
			Converter<InfixExpression, RelationExpression> toRelationExpressionConverter,
			Converter<InfixExpression, MultiplicativeExpression> toMultiplicativeExpressionConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			Converter<InfixExpression, EqualityExpression> toEqualityExpressionConverter,
			Converter<InfixExpression, AdditiveExpression> toAdditiveExpressionConverter,
			ExpressionsFactory expressionsFactory) {
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
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		InfixExpression infix = (InfixExpression) expr;
		if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
			return handleConditionalOr(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
			return handleConditionalAnd(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.OR) {
			return handleOperatorOr(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.XOR) {
			return handleOperatorXor(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.AND) {
			return handleOperatorAnd(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.EQUALS
				|| infix.getOperator() == InfixExpression.Operator.NOT_EQUALS) {
			return this.toEqualityExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.GREATER
				|| infix.getOperator() == InfixExpression.Operator.GREATER_EQUALS
				|| infix.getOperator() == InfixExpression.Operator.LESS
				|| infix.getOperator() == InfixExpression.Operator.LESS_EQUALS) {
			return this.toRelationExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.LEFT_SHIFT
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_SIGNED
				|| infix.getOperator() == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
			return this.toShiftExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.PLUS
				|| infix.getOperator() == InfixExpression.Operator.MINUS) {
			return this.toAdditiveExpressionConverter.convert(infix);
		} else if (infix.getOperator() == InfixExpression.Operator.TIMES
				|| infix.getOperator() == InfixExpression.Operator.DIVIDE
				|| infix.getOperator() == InfixExpression.Operator.REMAINDER) {
			return this.toMultiplicativeExpressionConverter.convert(infix);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleOperatorAnd(InfixExpression infix) {
		org.emftext.language.java.expressions.AndExpression result;
		org.emftext.language.java.expressions.Expression ex = this.toExpressionConverter
				.convert(infix.getLeftOperand());
		if (ex instanceof org.emftext.language.java.expressions.AndExpression) {
			result = (org.emftext.language.java.expressions.AndExpression) ex;
		} else {
			result = this.expressionsFactory.createAndExpression();
			result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) ex);
		}
		result.getChildren().add((org.emftext.language.java.expressions.AndExpressionChild) this.toExpressionConverter
				.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren()
						.add((org.emftext.language.java.expressions.AndExpressionChild) this.toExpressionConverter
								.convert((Expression) obj)));
		this.utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleOperatorXor(InfixExpression infix) {
		org.emftext.language.java.expressions.ExclusiveOrExpression result;
		org.emftext.language.java.expressions.Expression ex = this.toExpressionConverter
				.convert(infix.getLeftOperand());
		if (ex instanceof org.emftext.language.java.expressions.ExclusiveOrExpression) {
			result = (org.emftext.language.java.expressions.ExclusiveOrExpression) ex;
		} else {
			result = this.expressionsFactory.createExclusiveOrExpression();
			result.getChildren().add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) ex);
		}
		result.getChildren()
				.add((org.emftext.language.java.expressions.ExclusiveOrExpressionChild) this.toExpressionConverter
						.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren().add(
						(org.emftext.language.java.expressions.ExclusiveOrExpressionChild) this.toExpressionConverter
								.convert((Expression) obj)));
		this.utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleOperatorOr(InfixExpression infix) {
		org.emftext.language.java.expressions.InclusiveOrExpression result;
		org.emftext.language.java.expressions.Expression ex = this.toExpressionConverter
				.convert(infix.getLeftOperand());
		if (ex instanceof org.emftext.language.java.expressions.InclusiveOrExpression) {
			result = (org.emftext.language.java.expressions.InclusiveOrExpression) ex;
		} else {
			result = this.expressionsFactory.createInclusiveOrExpression();
			result.getChildren().add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) ex);
		}
		result.getChildren()
				.add((org.emftext.language.java.expressions.InclusiveOrExpressionChild) this.toExpressionConverter
						.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren().add(
						(org.emftext.language.java.expressions.InclusiveOrExpressionChild) this.toExpressionConverter
								.convert((Expression) obj)));
		this.utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleConditionalAnd(InfixExpression infix) {
		org.emftext.language.java.expressions.ConditionalAndExpression result;
		org.emftext.language.java.expressions.Expression ex = this.toExpressionConverter
				.convert(infix.getLeftOperand());
		if (ex instanceof org.emftext.language.java.expressions.ConditionalAndExpression) {
			result = (org.emftext.language.java.expressions.ConditionalAndExpression) ex;
		} else {
			result = this.expressionsFactory.createConditionalAndExpression();
			result.getChildren().add((ConditionalAndExpressionChild) ex);
		}
		result.getChildren()
				.add((ConditionalAndExpressionChild) this.toExpressionConverter.convert(infix.getRightOperand()));
		infix.extendedOperands().forEach(obj -> result.getChildren()
				.add((ConditionalAndExpressionChild) this.toExpressionConverter.convert((Expression) obj)));
		this.utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.expressions.Expression handleConditionalOr(InfixExpression infix) {
		org.emftext.language.java.expressions.ConditionalOrExpression result;
		org.emftext.language.java.expressions.Expression ex = this.toExpressionConverter
				.convert(infix.getLeftOperand());
		if (ex instanceof org.emftext.language.java.expressions.ConditionalOrExpression) {
			result = (org.emftext.language.java.expressions.ConditionalOrExpression) ex;
		} else {
			result = this.expressionsFactory.createConditionalOrExpression();
			result.getChildren().add((ConditionalOrExpressionChild) ex);
		}
		result.getChildren()
				.add((ConditionalOrExpressionChild) this.toExpressionConverter.convert(infix.getRightOperand()));
		infix.extendedOperands().forEach(obj -> result.getChildren()
				.add((ConditionalOrExpressionChild) this.toExpressionConverter.convert((Expression) obj)));
		this.utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

}