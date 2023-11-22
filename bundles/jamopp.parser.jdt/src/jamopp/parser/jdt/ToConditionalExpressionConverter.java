package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ConditionalExpression;

public class ToConditionalExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;

	public ToConditionalExpressionConverter(ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;

	}

	org.emftext.language.java.expressions.ConditionalExpression convertToConditionalExpression(
			ConditionalExpression expr) {
		org.emftext.language.java.expressions.ConditionalExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createConditionalExpression();
		result.setChild((org.emftext.language.java.expressions.ConditionalExpressionChild) toExpressionConverter
				.convertToExpression(expr.getExpression()));
		result.setExpressionIf(toExpressionConverter.convertToExpression(expr.getThenExpression()));
		result.setGeneralExpressionElse(toExpressionConverter.convertToExpression(expr.getElseExpression()));
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
