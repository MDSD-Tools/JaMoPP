package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToConditionalExpressionConverter
		implements Converter<org.eclipse.jdt.core.dom.ConditionalExpression, ConditionalExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	public ToConditionalExpressionConverter(
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final UtilLayout layoutInformationConverter, final ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
	}

	@Override
	public ConditionalExpression convert(final org.eclipse.jdt.core.dom.ConditionalExpression expr) {
		final ConditionalExpression result = expressionsFactory.createConditionalExpression();
		result.setChild((ConditionalExpressionChild) toExpressionConverter.convert(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convert(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convert(expr.getElseExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
