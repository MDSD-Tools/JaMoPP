package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

class ToAdditiveExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;
	private final ToAdditiveOperatorConverter toAdditiveOperatorConverter;

	ToAdditiveExpressionConverter(ToExpressionConverter toExpressionConverter,
			ToAdditiveOperatorConverter toAdditiveOperatorConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toAdditiveOperatorConverter = toAdditiveOperatorConverter;

	}

	@SuppressWarnings("unchecked")
	AdditiveExpression convertToAdditiveExpression(InfixExpression expr) {
		AdditiveExpression result = ExpressionsFactory.eINSTANCE.createAdditiveExpression();
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

	private void mergeAdditiveExpressionAndExpression(AdditiveExpression addExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof AdditiveExpressionChild a) {
			addExpr.getChildren().add((AdditiveExpressionChild) potChild);
		} else {
			AdditiveExpression expr = (AdditiveExpression) potChild;
			addExpr.getChildren().addAll(expr.getChildren());
			addExpr.getAdditiveOperators().addAll(expr.getAdditiveOperators());
		}
	}

}
