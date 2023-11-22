package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

public class ToAdditiveExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;
	private final ToAdditiveOperatorConverter toAdditiveOperatorConverter;
	
	public ToAdditiveExpressionConverter(ToExpressionConverter toExpressionConverter, ToAdditiveOperatorConverter toAdditiveOperatorConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toAdditiveOperatorConverter = toAdditiveOperatorConverter;
		
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.AdditiveExpression convertToAdditiveExpression(InfixExpression expr) {
		org.emftext.language.java.expressions.AdditiveExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createAdditiveExpression();
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getAdditiveOperators().add(toAdditiveOperatorConverter.convertToAdditiveOperator(expr.getOperator()));
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getAdditiveOperators()
					.add(toAdditiveOperatorConverter.convertToAdditiveOperator(expr.getOperator()));
			mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convertToExpression((Expression) obj));
		});
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private void mergeAdditiveExpressionAndExpression(org.emftext.language.java.expressions.AdditiveExpression addExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.AdditiveExpressionChild) {
			addExpr.getChildren().add((org.emftext.language.java.expressions.AdditiveExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.AdditiveExpression expr = (org.emftext.language.java.expressions.AdditiveExpression) potChild;
			addExpr.getChildren().addAll(expr.getChildren());
			addExpr.getAdditiveOperators().addAll(expr.getAdditiveOperators());
		}
	}

}
