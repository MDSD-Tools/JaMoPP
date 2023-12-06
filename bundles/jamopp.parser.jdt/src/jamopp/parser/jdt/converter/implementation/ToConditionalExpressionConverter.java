package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToConditionalExpressionConverter
		implements ToConverter<org.eclipse.jdt.core.dom.ConditionalExpression, ConditionalExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	ToConditionalExpressionConverter(
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
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
