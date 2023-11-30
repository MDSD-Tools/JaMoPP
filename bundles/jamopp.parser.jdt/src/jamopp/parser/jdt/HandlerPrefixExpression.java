package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class HandlerPrefixExpression {

	private final ExpressionsFactory expressionsFactory;
	private final ToUnaryExpressionConverter toUnaryExpressionConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;

	@Inject
	HandlerPrefixExpression(UtilLayout utilLayout, ToUnaryExpressionConverter toUnaryExpressionConverter,
			ToExpressionConverter toExpressionConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.toUnaryExpressionConverter = toUnaryExpressionConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	org.emftext.language.java.expressions.Expression handlePrefixExpression(Expression expr) {
		PrefixExpression prefixExpr = (PrefixExpression) expr;
		if (prefixExpr.getOperator() == PrefixExpression.Operator.COMPLEMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.NOT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.PLUS
				|| prefixExpr.getOperator() == PrefixExpression.Operator.MINUS) {
			return toUnaryExpressionConverter.convertToUnaryExpression(prefixExpr);
		}
		if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT
				|| prefixExpr.getOperator() == PrefixExpression.Operator.INCREMENT) {
			org.emftext.language.java.expressions.PrefixUnaryModificationExpression result = expressionsFactory
					.createPrefixUnaryModificationExpression();
			if (prefixExpr.getOperator() == PrefixExpression.Operator.DECREMENT) {
				result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createMinusMinus());
			} else {
				result.setOperator(org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createPlusPlus());
			}
			result.setChild(
					(org.emftext.language.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
							.convertToExpression(prefixExpr.getOperand()));
			utilLayout.convertToMinimalLayoutInformation(result, prefixExpr);
			return result;
		}
		return null;
	}

}
