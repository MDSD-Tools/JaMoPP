package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.ShiftOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToShiftOperatorConverter implements ToConverter<InfixExpression.Operator, ShiftOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToShiftOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public ShiftOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.LEFT_SHIFT) {
			return operatorsFactory.createLeftShift();
		}
		if (op == InfixExpression.Operator.RIGHT_SHIFT_SIGNED) {
			return operatorsFactory.createRightShift();
		}
		if (op == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
			return operatorsFactory.createUnsignedRightShift();
		}
		return null;
	}

}
