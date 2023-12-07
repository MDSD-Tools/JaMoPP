package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Assignment;
import org.emftext.language.java.operators.AssignmentOperator;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;

public class ToAssignmentConverter implements Converter<Assignment.Operator, AssignmentOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToAssignmentConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public AssignmentOperator convert(Assignment.Operator op) {
		if (op == Assignment.Operator.ASSIGN) {
			return operatorsFactory.createAssignment();
		}
		if (op == Assignment.Operator.BIT_AND_ASSIGN) {
			return operatorsFactory.createAssignmentAnd();
		}
		if (op == Assignment.Operator.BIT_OR_ASSIGN) {
			return operatorsFactory.createAssignmentOr();
		}
		if (op == Assignment.Operator.BIT_XOR_ASSIGN) {
			return operatorsFactory.createAssignmentExclusiveOr();
		}
		if (op == Assignment.Operator.DIVIDE_ASSIGN) {
			return operatorsFactory.createAssignmentDivision();
		}
		if (op == Assignment.Operator.LEFT_SHIFT_ASSIGN) {
			return operatorsFactory.createAssignmentLeftShift();
		}
		if (op == Assignment.Operator.MINUS_ASSIGN) {
			return operatorsFactory.createAssignmentMinus();
		}
		if (op == Assignment.Operator.PLUS_ASSIGN) {
			return operatorsFactory.createAssignmentPlus();
		}
		if (op == Assignment.Operator.REMAINDER_ASSIGN) {
			return operatorsFactory.createAssignmentModulo();
		}
		if (op == Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN) {
			return operatorsFactory.createAssignmentRightShift();
		}
		if (op == Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN) {
			return operatorsFactory.createAssignmentUnsignedRightShift();
		}
		return operatorsFactory.createAssignmentMultiplication();
	}

}
