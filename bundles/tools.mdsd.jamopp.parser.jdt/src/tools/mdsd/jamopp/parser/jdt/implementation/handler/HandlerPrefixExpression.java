package tools.mdsd.jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.handler.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerPrefixExpression implements ExpressionHandler {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<PrefixExpression, UnaryExpression> toUnaryExpressionConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerPrefixExpression(UtilLayout utilLayout,
			Converter<PrefixExpression, UnaryExpression> toUnaryExpressionConverter,
			Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory, OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toUnaryExpressionConverter = toUnaryExpressionConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		PrefixExpression prefixExpr = (PrefixExpression) expr;
		if (prefixExpr.getOperator() == PrefixExpression.Operator.COMPLEMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.NOT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.PLUS
				|| prefixExpr.getOperator() == PrefixExpression.Operator.MINUS) {
			return toUnaryExpressionConverter.convert(prefixExpr);
		}
		if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.INCREMENT) {
			tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression result = expressionsFactory
					.createPrefixUnaryModificationExpression();
			if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT) {
				result.setOperator(operatorsFactory.createMinusMinus());
			} else {
				result.setOperator(operatorsFactory.createPlusPlus());
			}
			result.setChild(
					(tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
							.convert(prefixExpr.getOperand()));
			utilLayout.convertToMinimalLayoutInformation(result, prefixExpr);
			return result;
		}
		return null;
	}

}
