package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;

public class ToConditionalExpressionConverter
		implements Converter<org.eclipse.jdt.core.dom.ConditionalExpression, org.emftext.language.java.expressions.ConditionalExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	ToConditionalExpressionConverter(
			Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			IUtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
	}

	@Override
	public ConditionalExpression convert(org.eclipse.jdt.core.dom.ConditionalExpression expr) {
		ConditionalExpression result = expressionsFactory.createConditionalExpression();
		result.setChild((ConditionalExpressionChild) toExpressionConverter.convert(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convert(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convert(expr.getElseExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}