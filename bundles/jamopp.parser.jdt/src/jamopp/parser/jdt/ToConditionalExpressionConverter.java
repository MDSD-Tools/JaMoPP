package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class ToConditionalExpressionConverter {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;

	@Inject
	ToConditionalExpressionConverter(ToExpressionConverter toExpressionConverter, UtilLayout layoutInformationConverter,
			ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
	}

	ConditionalExpression convertToConditionalExpression(org.eclipse.jdt.core.dom.ConditionalExpression expr) {
		ConditionalExpression result = expressionsFactory.createConditionalExpression();
		result.setChild((ConditionalExpressionChild) toExpressionConverter.convert(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convert(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convert(expr.getElseExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
