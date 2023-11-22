package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

public class ToEqualityExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;
	private final ToEqualityOperatorConverter toEqualityOperatorConverter;
	
	public ToEqualityExpressionConverter(ToExpressionConverter toExpressionConverter, ToEqualityOperatorConverter toEqualityOperatorConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toEqualityOperatorConverter = toEqualityOperatorConverter;
		
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.EqualityExpression convertToEqualityExpression(InfixExpression expr) {
		org.emftext.language.java.expressions.EqualityExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createEqualityExpression();
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getEqualityOperators().add(toEqualityOperatorConverter.convertToEqualityOperator(expr.getOperator()));
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getEqualityOperators().add(toEqualityOperatorConverter.convertToEqualityOperator(expr.getOperator()));
			mergeEqualityExpressionAndExpression(result, toExpressionConverter.convertToExpression((Expression) obj));
		});
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}
	
	void mergeEqualityExpressionAndExpression(org.emftext.language.java.expressions.EqualityExpression eqExpr, org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.EqualityExpressionChild) {
			eqExpr.getChildren().add((org.emftext.language.java.expressions.EqualityExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.EqualityExpression expr = (org.emftext.language.java.expressions.EqualityExpression) potChild;
			eqExpr.getChildren().addAll(expr.getChildren());
			eqExpr.getEqualityOperators().addAll(expr.getEqualityOperators());
		}
	}

}
