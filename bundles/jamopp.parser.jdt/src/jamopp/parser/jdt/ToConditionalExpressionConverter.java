package jamopp.parser.jdt;

import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

class ToConditionalExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;

	ToConditionalExpressionConverter(ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
	}

	ConditionalExpression convertToConditionalExpression(org.eclipse.jdt.core.dom.ConditionalExpression expr) {
		ConditionalExpression result = ExpressionsFactory.eINSTANCE.createConditionalExpression();
		result.setChild((ConditionalExpressionChild) toExpressionConverter.convertToExpression(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convertToExpression(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convertToExpression(expr.getElseExpression()));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
