package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerPostfixExpression implements ExpressionHandler {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerPostfixExpression(UtilLayout utilLayout,
			ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			ExpressionsFactory expressionsFactory, OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		PostfixExpression postfixExpr = (PostfixExpression) expr;
		org.emftext.language.java.expressions.SuffixUnaryModificationExpression result = expressionsFactory
				.createSuffixUnaryModificationExpression();
		if (postfixExpr.getOperator() == PostfixExpression.Operator.DECREMENT) {
			result.setOperator(operatorsFactory.createMinusMinus());
		} else {
			result.setOperator(operatorsFactory.createPlusPlus());
		}
		result.setChild((org.emftext.language.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
				.convert(postfixExpr.getOperand()));
		utilLayout.convertToMinimalLayoutInformation(result, postfixExpr);
		return result;
	}

}
