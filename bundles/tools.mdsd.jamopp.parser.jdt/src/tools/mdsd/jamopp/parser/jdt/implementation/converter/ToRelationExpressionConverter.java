package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.RelationExpression;
import org.emftext.language.java.operators.RelationOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToRelationExpressionConverter implements Converter<InfixExpression, RelationExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, RelationOperator> toRelationOperatorConverter;

	@Inject
	ToRelationExpressionConverter(Converter<InfixExpression.Operator, RelationOperator> toRelationOperatorConverter,
			Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toRelationOperatorConverter = toRelationOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	public RelationExpression convert(InfixExpression expr) {
		org.emftext.language.java.expressions.RelationExpression result = expressionsFactory.createRelationExpression();
		mergeRelationExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getRelationOperators().add(toRelationOperatorConverter.convert(expr.getOperator()));
		mergeRelationExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getRelationOperators().add(toRelationOperatorConverter.convert(expr.getOperator()));
			mergeRelationExpressionAndExpression(result, toExpressionConverter.convert((Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
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