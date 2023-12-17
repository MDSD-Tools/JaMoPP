package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToShiftOperatorConverter implements Converter<InfixExpression.Operator, ShiftOperator> {

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
