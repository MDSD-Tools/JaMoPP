package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.operators.EqualityOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilLayout;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToEqualityExpressionConverter implements ToConverter<InfixExpression, EqualityExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter;
	private final ToConverter<InfixExpression.Operator, EqualityOperator> toEqualityOperatorConverter;

	@Inject
	ToEqualityExpressionConverter(ToConverter<org.eclipse.jdt.core.dom.Expression, Expression> toExpressionConverter,
			ToConverter<InfixExpression.Operator, EqualityOperator> toEqualityOperatorConverter,
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
