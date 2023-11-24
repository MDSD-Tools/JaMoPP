package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

class ToShiftExpressionConverter {

	private final LayoutInformationConverter layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final ToShiftOperatorConverter toShiftOperatorConverter;
	private final ExpressionsFactory expressionsFactory;

	ToShiftExpressionConverter(ToShiftOperatorConverter toShiftOperatorConverter,
			ToExpressionConverter toExpressionConverter, LayoutInformationConverter layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toShiftOperatorConverter = toShiftOperatorConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.ShiftExpression convertToShiftExpression(InfixExpression expr) {
		ShiftExpression result = expressionsFactory.createShiftExpression();
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getShiftOperators().add(toShiftOperatorConverter.convertToShiftOperator(expr.getOperator()));
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getShiftOperators().add(toShiftOperatorConverter.convertToShiftOperator(expr.getOperator()));
			mergeShiftExpressionAndExpression(result, toExpressionConverter.convertToExpression((Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeShiftExpressionAndExpression(ShiftExpression shiftExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof ShiftExpressionChild) {
			shiftExpr.getChildren().add((ShiftExpressionChild) potChild);
		} else {
			ShiftExpression expr = (ShiftExpression) potChild;
			shiftExpr.getChildren().addAll(expr.getChildren());
			shiftExpr.getShiftOperators().addAll(expr.getShiftOperators());
		}
	}

}
