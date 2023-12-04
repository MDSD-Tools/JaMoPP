package jamopp.parser.jdt.converter.other;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToShiftExpressionConverter implements ToConverter<InfixExpression, ShiftExpression> {

	private final UtilLayout layoutInformationConverter;
	private final ToExpressionConverter toExpressionConverter;
	private final ToShiftOperatorConverter toShiftOperatorConverter;
	private final ExpressionsFactory expressionsFactory;

	@Inject
	ToShiftExpressionConverter(ToShiftOperatorConverter toShiftOperatorConverter,
			ToExpressionConverter toExpressionConverter, UtilLayout layoutInformationConverter,
			ExpressionsFactory expressionsFactory) {
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