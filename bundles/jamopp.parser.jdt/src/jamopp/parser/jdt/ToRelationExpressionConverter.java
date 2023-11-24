package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

class ToRelationExpressionConverter {

	private final ToExpressionConverter toExpressionConverter;
	private final ToRelationOperatorConverter toRelationOperatorConverter;

	public ToRelationExpressionConverter(ToRelationOperatorConverter toRelationOperatorConverter,
			ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.toRelationOperatorConverter = toRelationOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.expressions.RelationExpression convertToRelationExpression(InfixExpression expr) {
		org.emftext.language.java.expressions.RelationExpression result = org.emftext.language.java.expressions.ExpressionsFactory.eINSTANCE
				.createRelationExpression();
		mergeRelationExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getLeftOperand()));
		result.getRelationOperators().add(toRelationOperatorConverter.convertToRelationOperator(expr.getOperator()));
		mergeRelationExpressionAndExpression(result, toExpressionConverter.convertToExpression(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getRelationOperators()
					.add(toRelationOperatorConverter.convertToRelationOperator(expr.getOperator()));
			mergeRelationExpressionAndExpression(result, toExpressionConverter.convertToExpression((Expression) obj));
		});
		LayoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeRelationExpressionAndExpression(org.emftext.language.java.expressions.RelationExpression relExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof org.emftext.language.java.expressions.RelationExpressionChild) {
			relExpr.getChildren().add((org.emftext.language.java.expressions.RelationExpressionChild) potChild);
		} else {
			org.emftext.language.java.expressions.RelationExpression expr = (org.emftext.language.java.expressions.RelationExpression) potChild;
			relExpr.getChildren().addAll(expr.getChildren());
			relExpr.getRelationOperators().addAll(expr.getRelationOperators());
		}
	}

}
