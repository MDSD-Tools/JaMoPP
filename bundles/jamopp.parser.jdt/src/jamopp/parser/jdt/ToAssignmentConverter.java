package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Assignment;

public class ToAssignmentConverter {

	org.emftext.language.java.operators.AssignmentOperator convertToAssignmentOperator(Assignment.Operator op) {
		if (op == Assignment.Operator.ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignment();
		}
		if (op == Assignment.Operator.BIT_AND_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentAnd();
		}
		if (op == Assignment.Operator.BIT_OR_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentOr();
		}
		if (op == Assignment.Operator.BIT_XOR_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentExclusiveOr();
		}
		if (op == Assignment.Operator.DIVIDE_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentDivision();
		}
		if (op == Assignment.Operator.LEFT_SHIFT_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentLeftShift();
		}
		if (op == Assignment.Operator.MINUS_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentMinus();
		}
		if (op == Assignment.Operator.PLUS_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentPlus();
		}
		if (op == Assignment.Operator.REMAINDER_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentModulo();
		}
		if (op == Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentRightShift();
		}
		if (op == Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentUnsignedRightShift();
		}
		return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAssignmentMultiplication();
	}
	
}
