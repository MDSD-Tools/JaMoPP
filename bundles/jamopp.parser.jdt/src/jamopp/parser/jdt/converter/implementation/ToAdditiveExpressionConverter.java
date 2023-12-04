package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToAdditiveExpressionConverter {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final ToAdditiveOperatorConverter toAdditiveOperatorConverter;

	@Inject
	ToAdditiveExpressionConverter(ToExpressionConverter toExpressionConverter,
			ToAdditiveOperatorConverter toAdditiveOperatorConverter, UtilLayout layoutInformationConverter,
			ExpressionsFactory expressionsFactory) {
		this.expressionsFactory = expressionsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toAdditiveOperatorConverter = toAdditiveOperatorConverter;
	}

	@SuppressWarnings("unchecked")
	public
	AdditiveExpression convertToAdditiveExpression(InfixExpression expr) {
		AdditiveExpression result = expressionsFactory.createAdditiveExpression();
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getAdditiveOperators().add(toAdditiveOperatorConverter.convertToAdditiveOperator(expr.getOperator()));
		mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getAdditiveOperators()
					.add(toAdditiveOperatorConverter.convertToAdditiveOperator(expr.getOperator()));
			mergeAdditiveExpressionAndExpression(result, toExpressionConverter.convert((Expression) obj));
		});

		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
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
