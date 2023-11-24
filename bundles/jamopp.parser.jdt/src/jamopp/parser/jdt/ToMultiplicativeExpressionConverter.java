package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

class ToMultiplicativeExpressionConverter {

	private static final LayoutInformationConverter LayoutInformationConverter = new LayoutInformationConverter();
	
	private final ToExpressionConverter toExpressionConverter;
	private final ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter;
	
	ToMultiplicativeExpressionConverter(ToMultiplicativeOperatorConverter toMultiplicativeOperatorConverter, ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toMultiplicativeOperatorConverter = toMultiplicativeOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.MultiplicativeExpression convertToMultiplicativeExpression(
			InfixExpression expr) {
		org.emftext.language.java.expressions.MultiplicativeExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createMultiplicativeExpression();
		mergeMultiplicativeExpressionAndExpression(result,
				toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getMultiplicativeOperators()
				.add(toMultiplicativeOperatorConverter.convertToMultiplicativeOperator(expr.getOperator()));
		mergeMultiplicativeExpressionAndExpression(result,
				toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getMultiplicativeOperators()
					.add(toMultiplicativeOperatorConverter.convertToMultiplicativeOperator(expr.getOperator()));
			mergeMultiplicativeExpressionAndExpression(result,
					toExpressionConverter.convertToExpression((Expression) obj));
		});
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeMultiplicativeExpressionAndExpression(
			org.emftext.language.java.expressions.MultiplicativeExpression mulExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.MultiplicativeExpressionChild) {
			mulExpr.getChildren().add((org.emftext.language.java.expressions.MultiplicativeExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.MultiplicativeExpression expr = (org.emftext.language.java.expressions.MultiplicativeExpression) potChild;
			mulExpr.getChildren().addAll(expr.getChildren());
			mulExpr.getMultiplicativeOperators().addAll(expr.getMultiplicativeOperators());
		}
	}

}
