package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

class HandlerPostfixExpression {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;

	@Inject
	HandlerPostfixExpression(UtilLayout utilLayout, ToExpressionConverter toExpressionConverter,
			ExpressionsFactory expressionsFactory, OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	org.emftext.language.java.expressions.Expression handlePostfixExpression(Expression expr) {
		PostfixExpression postfixExpr = (PostfixExpression) expr;
		org.emftext.language.java.expressions.SuffixUnaryModificationExpression result = expressionsFactory
				.createSuffixUnaryModificationExpression();
		if (postfixExpr.getOperator() == PostfixExpression.Operator.DECREMENT) {
			result.setOperator(operatorsFactory.createMinusMinus());
		} else {
			result.setOperator(operatorsFactory.createPlusPlus());
		}
		result.setChild((org.emftext.language.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
				.convertToExpression(postfixExpr.getOperand()));
		utilLayout.convertToMinimalLayoutInformation(result, postfixExpr);
		return result;
	}

}
