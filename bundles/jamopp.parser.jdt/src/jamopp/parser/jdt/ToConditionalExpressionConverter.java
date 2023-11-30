package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class ToConditionalExpressionConverter {

	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;

	@Inject
	ToConditionalExpressionConverter(ToExpressionConverter toExpressionConverter, UtilLayout layoutInformationConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
	}

	ConditionalExpression convertToConditionalExpression(org.eclipse.jdt.core.dom.ConditionalExpression expr) {
		ConditionalExpression result = ExpressionsFactory.eINSTANCE.createConditionalExpression();
		result.setChild((ConditionalExpressionChild) toExpressionConverter.convertToExpression(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convertToExpression(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convertToExpression(expr.getElseExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
