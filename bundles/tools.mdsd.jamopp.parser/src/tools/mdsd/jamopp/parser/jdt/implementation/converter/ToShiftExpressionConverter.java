package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToShiftExpressionConverter implements Converter<InfixExpression, ShiftExpression> {

	private final ExpressionsFactory expressionsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;
	private final Converter<InfixExpression.Operator, ShiftOperator> toShiftOperatorConverter;

	@Inject
	public ToShiftExpressionConverter(final Converter<InfixExpression.Operator, ShiftOperator> toShiftOperatorConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			final UtilLayout layoutInformationConverter, final ExpressionsFactory expressionsFactory) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.toExpressionConverter = toExpressionConverter;
		this.toShiftOperatorConverter = toShiftOperatorConverter;
		this.expressionsFactory = expressionsFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShiftExpression convert(final InfixExpression expr) {
		final ShiftExpression result = expressionsFactory.createShiftExpression();
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

	private void mergeShiftExpressionAndExpression(final ShiftExpression shiftExpr,
			final tools.mdsd.jamopp.model.java.expressions.Expression potChild) {
		if (potChild instanceof ShiftExpressionChild) {
			shiftExpr.getChildren().add((ShiftExpressionChild) potChild);
		} else {
			final ShiftExpression expr = (ShiftExpression) potChild;
			shiftExpr.getChildren().addAll(expr.getChildren());
			shiftExpr.getShiftOperators().addAll(expr.getShiftOperators());
		}
	}

}
