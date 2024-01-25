package tools.mdsd.jamopp.parser.implementation.converter.expression;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class HandlerPrefixExpression implements ExpressionHandler {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<PrefixExpression, UnaryExpression> toUnaryExpressionConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	public HandlerPrefixExpression(final UtilLayout utilLayout,
			final Converter<PrefixExpression, UnaryExpression> toUnaryExpressionConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final ExpressionsFactory expressionsFactory, final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toUnaryExpressionConverter = toUnaryExpressionConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(final Expression expr) {
		final PrefixExpression prefixExpr = (PrefixExpression) expr;
		tools.mdsd.jamopp.model.java.expressions.Expression expression = null;
		if (prefixExpr.getOperator().equals(PrefixExpression.Operator.COMPLEMENT)
				|| prefixExpr.getOperator().equals(PrefixExpression.Operator.NOT)
				|| prefixExpr.getOperator().equals(PrefixExpression.Operator.PLUS)
				|| prefixExpr.getOperator().equals(PrefixExpression.Operator.MINUS)) {
			expression = toUnaryExpressionConverter.convert(prefixExpr);
		} else if (prefixExpr.getOperator().equals(PrefixExpression.Operator.DECREMENT)
				|| prefixExpr.getOperator().equals(PrefixExpression.Operator.INCREMENT)) {
			final tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression result = expressionsFactory
					.createPrefixUnaryModificationExpression();
			if (prefixExpr.getOperator().equals(PrefixExpression.Operator.DECREMENT)) {
				result.setOperator(operatorsFactory.createMinusMinus());
			} else {
				result.setOperator(operatorsFactory.createPlusPlus());
			}
			result.setChild(
					(tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
							.convert(prefixExpr.getOperand()));
			utilLayout.convertToMinimalLayoutInformation(result, prefixExpr);
			expression = result;
		}
		return expression;
	}

}
