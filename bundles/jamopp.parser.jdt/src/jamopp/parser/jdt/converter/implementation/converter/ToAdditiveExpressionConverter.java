package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.AdditiveOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToAdditiveExpressionConverter implements ToConverter<InfixExpression, AdditiveExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final ToConverter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter;

	@Inject
	ToAdditiveExpressionConverter(
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			ToConverter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter,
			IUtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toAdditiveOperatorConverter = toAdditiveOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdditiveExpression convert(InfixExpression expr) {
		AdditiveExpression result = expressionsFactory.createAdditiveExpression();
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getAdditiveOperators().add(toAdditiveOperatorConverter.convert(expr.getOperator()));
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getAdditiveOperators().add(toAdditiveOperatorConverter.convert(expr.getOperator()));
			mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert((Expression) obj));
		});

		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private void mergeAdditiveExpressionAndExpression(AdditiveExpression addExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof AdditiveExpressionChild a) {
			addExpr.getChildren().add((AdditiveExpressionChild) potChild);
		} else {
			AdditiveExpression expr = (AdditiveExpression) potChild;
			addExpr.getChildren().addAll(expr.getChildren());
			addExpr.getAdditiveOperators().addAll(expr.getAdditiveOperators());
		}
	}

}
