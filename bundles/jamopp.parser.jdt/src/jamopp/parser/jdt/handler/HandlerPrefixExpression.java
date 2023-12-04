package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToUnaryExpressionConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerPrefixExpression extends Handler {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final ToUnaryExpressionConverter toUnaryExpressionConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;

	@Inject
	HandlerPrefixExpression(UtilLayout utilLayout, ToUnaryExpressionConverter toUnaryExpressionConverter,
			ToExpressionConverter toExpressionConverter, ExpressionsFactory expressionsFactory,
			OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toUnaryExpressionConverter = toUnaryExpressionConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		PrefixExpression prefixExpr = (PrefixExpression) expr;
		if (prefixExpr.getOperator() == PrefixExpression.Operator.COMPLEMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.NOT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.PLUS
				|| prefixExpr.getOperator() == PrefixExpression.Operator.MINUS) {
			return toUnaryExpressionConverter.convert(prefixExpr);
		}
		if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.INCREMENT) {
			org.emftext.language.java.expressions.PrefixUnaryModificationExpression result = expressionsFactory
					.createPrefixUnaryModificationExpression();
			if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT) {
				result.setOperator(operatorsFactory.createMinusMinus());
			} else {
				result.setOperator(operatorsFactory.createPlusPlus());
			}
			result.setChild(
					(org.emftext.language.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
							.convert(prefixExpr.getOperand()));
			utilLayout.convertToMinimalLayoutInformation(result, prefixExpr);
			return result;
		}
		return null;
	}

}
