package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.ShiftOperator;

class ToShiftOperatorConverter {

	private final OperatorsFactory operatorsFactory;
	
	public ToShiftOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;	
	}

	ShiftOperator convertToShiftOperator(InfixExpression.Operator op) {
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
