package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.PrefixExpression;

public class ToUnaryExpressionConverter {
	
	private final ToExpressionConverter toExpressionConverter;
	private final ToUnaryOperatorConverter toUnaryOperatorConverter;
	
	public ToUnaryExpressionConverter(ToUnaryOperatorConverter toUnaryOperatorConverter, ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toUnaryOperatorConverter = toUnaryOperatorConverter;
	}

	org.emftext.language.java.expressions.UnaryExpression convertToUnaryExpression(PrefixExpression expr) {
		org.emftext.language.java.expressions.UnaryExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createUnaryExpression();
		result.getOperators().add(toUnaryOperatorConverter.convertToUnaryOperator(expr.getOperator()));
		org.emftext.language.java.expressions.Expression potChild = toExpressionConverter.convertToExpression(expr.getOperand());
		if (potChild instanceof org.emftext.language.java.expressions.UnaryExpressionChild) {
			result.setChild((org.emftext.language.java.expressions.UnaryExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.UnaryExpression secRes = (org.emftext.language.java.expressions.UnaryExpression) potChild;
			result.getOperators().addAll(secRes.getOperators());
			result.setChild(secRes.getChild());
		}
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

}
