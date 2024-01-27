package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Assignment;

import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToAssignmentConverter implements Converter<Assignment.Operator, AssignmentOperator> {

	private final OperatorsFactory operatorsFactory;
	private final Map<Assignment.Operator, Supplier<AssignmentOperator>> mapping;

	@Inject
	public ToAssignmentConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
		mapping = new HashMap<>();
		mapping.put(Assignment.Operator.ASSIGN, () -> operatorsFactory.createAssignment());
		mapping.put(Assignment.Operator.BIT_AND_ASSIGN, () -> operatorsFactory.createAssignmentAnd());
		mapping.put(Assignment.Operator.BIT_OR_ASSIGN, () -> operatorsFactory.createAssignmentOr());
		mapping.put(Assignment.Operator.BIT_XOR_ASSIGN, () -> operatorsFactory.createAssignmentExclusiveOr());
		mapping.put(Assignment.Operator.DIVIDE_ASSIGN, () -> operatorsFactory.createAssignmentDivision());
		mapping.put(Assignment.Operator.LEFT_SHIFT_ASSIGN, () -> operatorsFactory.createAssignmentLeftShift());
		mapping.put(Assignment.Operator.MINUS_ASSIGN, () -> operatorsFactory.createAssignmentMinus());
		mapping.put(Assignment.Operator.PLUS_ASSIGN, () -> operatorsFactory.createAssignmentPlus());
		mapping.put(Assignment.Operator.REMAINDER_ASSIGN, () -> operatorsFactory.createAssignmentModulo());
		mapping.put(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN, () -> operatorsFactory.createAssignmentRightShift());
		mapping.put(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN,
				() -> operatorsFactory.createAssignmentUnsignedRightShift());

	}

	@Override
	public AssignmentOperator convert(final Assignment.Operator operator) {
		return mapping.getOrDefault(operator, () -> operatorsFactory.createAssignmentMultiplication()).get();
	}

}
