package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToRelationExpressionConverter implements Converter<InfixExpression, RelationExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, RelationOperator> toRelationOperatorConverter;

	@Inject
	ToRelationExpressionConverter(Converter<InfixExpression.Operator, RelationOperator> toRelationOperatorConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toRelationOperatorConverter = toRelationOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	public RelationExpression convert(InfixExpression expr) {
		tools.mdsd.jamopp.model.java.expressions.RelationExpression result = expressionsFactory.createRelationExpression();
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

	void mergeRelationExpressionAndExpression(tools.mdsd.jamopp.model.java.expressions.RelationExpression relExpr,
			tools.mdsd.jamopp.model.java.expressions.Expression potChild) {
		if (potChild instanceof tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild) {
			relExpr.getChildren().add((tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild) potChild);
		} else {
			tools.mdsd.jamopp.model.java.expressions.RelationExpression expr = (tools.mdsd.jamopp.model.java.expressions.RelationExpression) potChild;
			relExpr.getChildren().addAll(expr.getChildren());
			relExpr.getRelationOperators().addAll(expr.getRelationOperators());
		}
	}

}
