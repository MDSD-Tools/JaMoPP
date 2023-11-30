package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class ToEqualityExpressionConverter {

	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final ToEqualityOperatorConverter toEqualityOperatorConverter;

	@Inject
	ToEqualityExpressionConverter(ToExpressionConverter toExpressionConverter,
			ToEqualityOperatorConverter toEqualityOperatorConverter,
			UtilLayout layoutInformationConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toEqualityOperatorConverter = toEqualityOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	EqualityExpression convertToEqualityExpression(InfixExpression expr) {
		EqualityExpression result = ExpressionsFactory.eINSTANCE.createEqualityExpression();
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getEqualityOperators().add(toEqualityOperatorConverter.convertToEqualityOperator(expr.getOperator()));
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getEqualityOperators()
					.add(toEqualityOperatorConverter.convertToEqualityOperator(expr.getOperator()));
			mergeEqualityExpressionAndExpression(result,
					toExpressionConverter.convertToExpression((org.eclipse.jdt.core.dom.Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeEqualityExpressionAndExpression(EqualityExpression eqExpr, Expression potChild) {
		if (potChild instanceof EqualityExpressionChild) {
			eqExpr.getChildren().add((EqualityExpressionChild) potChild);
		} else {
			EqualityExpression expr = (EqualityExpression) potChild;
			eqExpr.getChildren().addAll(expr.getChildren());
			eqExpr.getEqualityOperators().addAll(expr.getEqualityOperators());
		}
	}

}
