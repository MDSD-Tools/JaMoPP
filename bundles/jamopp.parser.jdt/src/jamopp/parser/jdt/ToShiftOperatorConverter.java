package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;

public class ToShiftOperatorConverter {

	org.emftext.language.java.operators.ShiftOperator convertToShiftOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.LEFT_SHIFT) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createLeftShift();
		}
		if (op == InfixExpression.Operator.RIGHT_SHIFT_SIGNED) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createRightShift();
		}
		if (op == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createUnsignedRightShift();
		}
		return null;
	}
	
}
