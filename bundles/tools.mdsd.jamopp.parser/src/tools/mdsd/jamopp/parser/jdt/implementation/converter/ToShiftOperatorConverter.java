package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToShiftOperatorConverter implements Converter<InfixExpression.Operator, ShiftOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToShiftOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public ShiftOperator convert(InfixExpression.Operator operator) {
		ShiftOperator result = null;
		if (operator.equals(InfixExpression.Operator.LEFT_SHIFT)) {
			result = operatorsFactory.createLeftShift();
		} else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_SIGNED)) {
			result = operatorsFactory.createRightShift();
		} else if (operator.equals(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED)) {
			result = operatorsFactory.createUnsignedRightShift();
		}
		return result;
	}

}
