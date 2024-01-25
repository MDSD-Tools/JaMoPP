package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.RelationOperator;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToRelationOperatorConverter implements Converter<InfixExpression.Operator, RelationOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToRelationOperatorConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public RelationOperator convert(final InfixExpression.Operator operator) {
		RelationOperator result = null;
		if (operator.equals(InfixExpression.Operator.GREATER)) {
			result = operatorsFactory.createGreaterThan();
		} else if (operator.equals(InfixExpression.Operator.GREATER_EQUALS)) {
			result = operatorsFactory.createGreaterThanOrEqual();
		} else if (operator.equals(InfixExpression.Operator.LESS)) {
			result = operatorsFactory.createLessThan();
		} else if (operator.equals(InfixExpression.Operator.LESS_EQUALS)) {
			result = operatorsFactory.createLessThanOrEqual();
		}
		return result;
	}

}
