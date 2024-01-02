package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToRelationOperatorConverter implements Converter<InfixExpression.Operator, RelationOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToRelationOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	public RelationOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.GREATER) {
			return operatorsFactory.createGreaterThan();
		}
		if (op == InfixExpression.Operator.GREATER_EQUALS) {
			return operatorsFactory.createGreaterThanOrEqual();
		}
		if (op == InfixExpression.Operator.LESS) {
			return operatorsFactory.createLessThan();
		}
		if (op == InfixExpression.Operator.LESS_EQUALS) {
			return operatorsFactory.createLessThanOrEqual();
		}
		return null;
	}

}
