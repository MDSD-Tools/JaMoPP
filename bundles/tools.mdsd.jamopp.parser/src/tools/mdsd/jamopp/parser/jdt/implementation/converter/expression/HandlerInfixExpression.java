package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerInfixExpression implements ExpressionHandler {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Map<Function<InfixExpression, tools.mdsd.jamopp.model.java.expressions.Expression>, Set<InfixExpression.Operator>> mapping;

	@Inject
	public HandlerInfixExpression(UtilLayout utilLayout,
			Converter<InfixExpression, ShiftExpression> toShiftExpressionConverter,
			Converter<InfixExpression, RelationExpression> toRelationExpressionConverter,
			Converter<InfixExpression, MultiplicativeExpression> toMultiplicativeExpressionConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			Converter<InfixExpression, EqualityExpression> toEqualityExpressionConverter,
			Converter<InfixExpression, AdditiveExpression> toAdditiveExpressionConverter,
			ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		mapping = new HashMap<>();
		mapping.put(this::handleConditionalOr, Set.of(InfixExpression.Operator.CONDITIONAL_OR));
		mapping.put(this::handleConditionalAnd, Set.of(InfixExpression.Operator.CONDITIONAL_AND));
		mapping.put(this::handleOperatorOr, Set.of(InfixExpression.Operator.OR));
		mapping.put(this::handleOperatorXor, Set.of(InfixExpression.Operator.XOR));
		mapping.put(this::handleOperatorAnd, Set.of(InfixExpression.Operator.AND));
		mapping.put(toEqualityExpressionConverter::convert,
				Set.of(InfixExpression.Operator.EQUALS, InfixExpression.Operator.NOT_EQUALS));
		mapping.put(toRelationExpressionConverter::convert,
				Set.of(InfixExpression.Operator.GREATER, InfixExpression.Operator.GREATER_EQUALS,
						InfixExpression.Operator.LESS, InfixExpression.Operator.LESS_EQUALS));
		mapping.put(toShiftExpressionConverter::convert, Set.of(InfixExpression.Operator.LEFT_SHIFT,
				InfixExpression.Operator.RIGHT_SHIFT_SIGNED, InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED));
		mapping.put(toAdditiveExpressionConverter::convert,
				Set.of(InfixExpression.Operator.PLUS, InfixExpression.Operator.MINUS));
		mapping.put(toMultiplicativeExpressionConverter::convert, Set.of(InfixExpression.Operator.TIMES,
				InfixExpression.Operator.DIVIDE, InfixExpression.Operator.REMAINDER));
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		InfixExpression infix = (InfixExpression) expr;
		for (Entry<Function<InfixExpression, tools.mdsd.jamopp.model.java.expressions.Expression>, Set<Operator>> entry : mapping
				.entrySet()) {
			if (entry.getValue().contains(infix.getOperator())) {
				return entry.getKey().apply(infix);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.expressions.Expression handleOperatorAnd(InfixExpression infix) {
		tools.mdsd.jamopp.model.java.expressions.AndExpression result;
		tools.mdsd.jamopp.model.java.expressions.Expression ex = toExpressionConverter.convert(infix.getLeftOperand());
		if (ex instanceof tools.mdsd.jamopp.model.java.expressions.AndExpression) {
			result = (tools.mdsd.jamopp.model.java.expressions.AndExpression) ex;
		} else {
			result = expressionsFactory.createAndExpression();
			result.getChildren().add((tools.mdsd.jamopp.model.java.expressions.AndExpressionChild) ex);
		}
		result.getChildren().add((tools.mdsd.jamopp.model.java.expressions.AndExpressionChild) toExpressionConverter
				.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren()
						.add((tools.mdsd.jamopp.model.java.expressions.AndExpressionChild) toExpressionConverter
								.convert((Expression) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.expressions.Expression handleOperatorXor(InfixExpression infix) {
		tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression result;
		tools.mdsd.jamopp.model.java.expressions.Expression ex = toExpressionConverter.convert(infix.getLeftOperand());
		if (ex instanceof tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression) {
			result = (tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression) ex;
		} else {
			result = expressionsFactory.createExclusiveOrExpression();
			result.getChildren().add((tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild) ex);
		}
		result.getChildren()
				.add((tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild) toExpressionConverter
						.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren()
						.add((tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild) toExpressionConverter
								.convert((Expression) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.expressions.Expression handleOperatorOr(InfixExpression infix) {
		tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression result;
		tools.mdsd.jamopp.model.java.expressions.Expression ex = toExpressionConverter.convert(infix.getLeftOperand());
		if (ex instanceof tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression) {
			result = (tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression) ex;
		} else {
			result = expressionsFactory.createInclusiveOrExpression();
			result.getChildren().add((tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild) ex);
		}
		result.getChildren()
				.add((tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild) toExpressionConverter
						.convert(infix.getRightOperand()));
		infix.extendedOperands()
				.forEach(obj -> result.getChildren()
						.add((tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild) toExpressionConverter
								.convert((Expression) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.expressions.Expression handleConditionalAnd(InfixExpression infix) {
		tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression result;
		tools.mdsd.jamopp.model.java.expressions.Expression ex = toExpressionConverter.convert(infix.getLeftOperand());
		if (ex instanceof tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression) {
			result = (tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression) ex;
		} else {
			result = expressionsFactory.createConditionalAndExpression();
			result.getChildren().add((ConditionalAndExpressionChild) ex);
		}
		result.getChildren()
				.add((ConditionalAndExpressionChild) toExpressionConverter.convert(infix.getRightOperand()));
		infix.extendedOperands().forEach(obj -> result.getChildren()
				.add((ConditionalAndExpressionChild) toExpressionConverter.convert((Expression) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.expressions.Expression handleConditionalOr(InfixExpression infix) {
		tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression result;
		tools.mdsd.jamopp.model.java.expressions.Expression ex = toExpressionConverter.convert(infix.getLeftOperand());
		if (ex instanceof tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression) {
			result = (tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression) ex;
		} else {
			result = expressionsFactory.createConditionalOrExpression();
			result.getChildren().add((ConditionalOrExpressionChild) ex);
		}
		result.getChildren().add((ConditionalOrExpressionChild) toExpressionConverter.convert(infix.getRightOperand()));
		infix.extendedOperands().forEach(obj -> result.getChildren()
				.add((ConditionalOrExpressionChild) toExpressionConverter.convert((Expression) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, infix);
		return result;
	}

}
