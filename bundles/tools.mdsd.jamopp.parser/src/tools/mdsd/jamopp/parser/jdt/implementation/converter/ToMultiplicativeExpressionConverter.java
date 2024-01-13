package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToMultiplicativeExpressionConverter implements Converter<InfixExpression, MultiplicativeExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter;

	@Inject
	public ToMultiplicativeExpressionConverter(
			Converter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toMultiplicativeOperatorConverter = toMultiplicativeOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MultiplicativeExpression convert(InfixExpression expr) {
		MultiplicativeExpression result = expressionsFactory.createMultiplicativeExpression();
		mergeMultiplicativeExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getMultiplicativeOperators().add(toMultiplicativeOperatorConverter.convert(expr.getOperator()));
		mergeMultiplicativeExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getMultiplicativeOperators().add(toMultiplicativeOperatorConverter.convert(expr.getOperator()));
			mergeMultiplicativeExpressionAndExpression(result, toExpressionConverter.convert((Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private void mergeMultiplicativeExpressionAndExpression(MultiplicativeExpression mulExpr,
			tools.mdsd.jamopp.model.java.expressions.Expression potChild) {
		if (potChild instanceof tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild) {
			mulExpr.getChildren()
					.add((tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild) potChild);
		} else {
			MultiplicativeExpression expr = (MultiplicativeExpression) potChild;
			mulExpr.getChildren().addAll(expr.getChildren());
			mulExpr.getMultiplicativeOperators().addAll(expr.getMultiplicativeOperators());
		}
	}

}
