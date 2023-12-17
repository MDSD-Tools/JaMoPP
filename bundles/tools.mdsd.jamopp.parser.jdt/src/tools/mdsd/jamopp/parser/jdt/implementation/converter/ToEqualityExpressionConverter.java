package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.operators.EqualityOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToEqualityExpressionConverter implements Converter<InfixExpression, EqualityExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, EqualityOperator> toEqualityOperatorConverter;

	@Inject
	ToEqualityExpressionConverter(Converter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter,
			Converter<InfixExpression.Operator, EqualityOperator> toEqualityOperatorConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toEqualityOperatorConverter = toEqualityOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	public EqualityExpression convert(InfixExpression expr) {
		EqualityExpression result = expressionsFactory.createEqualityExpression();
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getEqualityOperators().add(toEqualityOperatorConverter.convert(expr.getOperator()));
		mergeEqualityExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getEqualityOperators().add(toEqualityOperatorConverter.convert(expr.getOperator()));
			mergeEqualityExpressionAndExpression(result,
					toExpressionConverter.convert((org.eclipse.jdt.core.dom.Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	private void mergeEqualityExpressionAndExpression(EqualityExpression eqExpr, Expression potChild) {
		if (potChild instanceof EqualityExpressionChild) {
			eqExpr.getChildren().add((EqualityExpressionChild) potChild);
		} else {
			EqualityExpression expr = (EqualityExpression) potChild;
			eqExpr.getChildren().addAll(expr.getChildren());
			eqExpr.getEqualityOperators().addAll(expr.getEqualityOperators());
		}
	}

}
