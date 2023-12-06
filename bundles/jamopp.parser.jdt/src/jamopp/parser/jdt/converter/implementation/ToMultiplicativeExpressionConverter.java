package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.operators.MultiplicativeOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToMultiplicativeExpressionConverter implements ToConverter<InfixExpression, MultiplicativeExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter;

	@Inject
	ToMultiplicativeExpressionConverter(
			ToConverter<InfixExpression.Operator, MultiplicativeOperator> toMultiplicativeOperatorConverter,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
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
