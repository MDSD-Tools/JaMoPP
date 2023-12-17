package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;
import org.emftext.language.java.operators.ShiftOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToShiftExpressionConverter implements Converter<InfixExpression, ShiftExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, ShiftOperator> toShiftOperatorConverter;

	@Inject
	ToShiftExpressionConverter(Converter<InfixExpression.Operator, ShiftOperator> toShiftOperatorConverter,
			Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			UtilLayout layoutInformationConverter, ExpressionsFactory expressionsFactory) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toShiftOperatorConverter = toShiftOperatorConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShiftExpression convert(InfixExpression expr) {
		ShiftExpression result = expressionsFactory.createShiftExpression();
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convert(expr.getLeftOperand()));
		result.getShiftOperators().add(toShiftOperatorConverter.convert(expr.getOperator()));
		mergeShiftExpressionAndExpression(result, toExpressionConverter.convert(expr.getRightOperand()));
		expr.extendedOperands().forEach(obj -> {
			result.getShiftOperators().add(toShiftOperatorConverter.convert(expr.getOperator()));
			mergeShiftExpressionAndExpression(result, toExpressionConverter.convert((Expression) obj));
		});
		layoutInformationConverter.convertToMinimalLayoutInformation(result, expr);
		return result;
	}

	void mergeShiftExpressionAndExpression(ShiftExpression shiftExpr,
			org.emftext.language.java.expressions.Expression potChild) {
		if (potChild instanceof ShiftExpressionChild) {
			shiftExpr.getChildren().add((ShiftExpressionChild) potChild);
		} else {
			ShiftExpression expr = (ShiftExpression) potChild;
			shiftExpr.getChildren().addAll(expr.getChildren());
			shiftExpr.getShiftOperators().addAll(expr.getShiftOperators());
		}
	}

}