package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

 class ToShiftExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;
	private final ToShiftOperatorConverter toShiftOperatorConverter;

	ToShiftExpressionConverter(ToShiftOperatorConverter toShiftOperatorConverter,
			ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toShiftOperatorConverter = toShiftOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.ShiftExpression convertToShiftExpression(InfixExpression expr) {
		org.emftext.language.java.expressions.ShiftExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createShiftExpression();
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getShiftOperators().add(toShiftOperatorConverter.convertToShiftOperator(expr.getOperator()));
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getShiftOperators().add(toShiftOperatorConverter.convertToShiftOperator(expr.getOperator()));
			mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression((Expression) obj));
		});
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeShiftExpressionAndExpression(org.emftext.language.java.expressions.ShiftExpression shiftExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.ShiftExpressionChild) {
			shiftExpr.getChildren().add((org.emftext.language.java.expressions.ShiftExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.ShiftExpression expr = (org.emftext.language.java.expressions.ShiftExpression) potChild;
			shiftExpr.getChildren().addAll(expr.getChildren());
			shiftExpr.getShiftOperators().addAll(expr.getShiftOperators());
		}
	}

}
