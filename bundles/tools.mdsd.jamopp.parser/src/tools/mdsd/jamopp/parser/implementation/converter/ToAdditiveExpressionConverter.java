package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ToAdditiveExpressionConverter implements Converter<InfixExpression, AdditiveExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter;

	@Inject
	public ToAdditiveExpressionConverter(
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final Converter<InfixExpression.Operator, AdditiveOperator> toAdditiveOperatorConverter,
			final UtilLayout layoutInformationConverter, final ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toAdditiveOperatorConverter = toAdditiveOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdditiveExpression convert(final InfixExpression expr) {
		final AdditiveExpression result = expressionsFactory.createAdditiveExpression();
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

	private void mergeAdditiveExpressionAndExpression(final AdditiveExpression addExpr,
			final tools.mdsd.jamopp.model.java.expressions.Expression potChild) {
		if (potChild instanceof AdditiveExpressionChild) {
			addExpr.getChildren().add((AdditiveExpressionChild) potChild);
		} else {
			final AdditiveExpression expr = (AdditiveExpression) potChild;
			addExpr.getChildren().addAll(expr.getChildren());
			addExpr.getAdditiveOperators().addAll(expr.getAdditiveOperators());
		}
	}

}
