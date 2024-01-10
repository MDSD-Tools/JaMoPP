package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToAdditiveExpressionConverter implements Converter<InfixExpression, AdditiveExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter;

	@Inject
	ToAdditiveExpressionConverter(
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			Converter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
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
			tools.mdsd.jamopp.model.java.expressions.Expression potChild) {
		if (potChild instanceof AdditiveExpressionChild a) {
			addExpr.getChildren().add((AdditiveExpressionChild) potChild);
		} else {
			AdditiveExpression expr = (AdditiveExpression) potChild;
			addExpr.getChildren().addAll(expr.getChildren());
			addExpr.getAdditiveOperators().addAll(expr.getAdditiveOperators());
		}
	}

}
