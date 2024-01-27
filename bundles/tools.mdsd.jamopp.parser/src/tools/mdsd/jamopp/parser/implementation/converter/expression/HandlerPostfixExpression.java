package tools.mdsd.jamopp.parser.implementation.converter.expression;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class HandlerPostfixExpression implements ExpressionHandler {

	private final OperatorsFactory operatorsFactory;
	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout utilLayout;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	public HandlerPostfixExpression(final UtilLayout utilLayout,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final ExpressionsFactory expressionsFactory, final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		this.expressionsFactory = expressionsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(final Expression expr) {
		final PostfixExpression postfixExpr = (PostfixExpression) expr;
		final tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression result = expressionsFactory
				.createSuffixUnaryModificationExpression();
		if (postfixExpr.getOperator().equals(PostfixExpression.Operator.DECREMENT)) {
			result.setOperator(operatorsFactory.createMinusMinus());
		} else {
			result.setOperator(operatorsFactory.createPlusPlus());
		}
		result.setChild(
				(tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild) toExpressionConverter
						.convert(postfixExpr.getOperand()));
		utilLayout.convertToMinimalLayoutInformation(result, postfixExpr);
		return result;
	}

}
