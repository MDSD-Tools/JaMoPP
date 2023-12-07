package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.operators.MultiplicativeOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToMultiplicativeExpressionConverter implements Converter<InfixExpression, MultiplicativeExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter;

	@Inject
	ToMultiplicativeExpressionConverter(
			Converter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter,
			Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			IUtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toMultiplicativeOperatorConverter = toMultiplicativeOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MultiplicativeExpression convert(InfixExpression expr) {
		org.emftext.language.java.expressions.MultiplicativeExpression result = expressionsFactory
				.createMultiplicativeExpression();
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

	private void mergeMultiplicativeExpressionAndExpression(
			org.emftext.language.java.expressions.MultiplicativeExpression mulExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.MultiplicativeExpressionChild) {
			mulExpr.getChildren().add((org.emftext.language.java.expressions.MultiplicativeExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.MultiplicativeExpression expr = (org.emftext.language.java.expressions.MultiplicativeExpression) potChild;
			mulExpr.getChildren().addAll(expr.getChildren());
			mulExpr.getMultiplicativeOperators().addAll(expr.getMultiplicativeOperators());
		}
	}

}
