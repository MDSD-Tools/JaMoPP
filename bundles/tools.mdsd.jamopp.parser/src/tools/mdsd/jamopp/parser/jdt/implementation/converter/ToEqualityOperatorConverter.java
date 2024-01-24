package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.operators.EqualityOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToEqualityOperatorConverter implements Converter<InfixExpression.Operator, EqualityOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToEqualityOperatorConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public EqualityOperator convert(final InfixExpression.Operator operator) {
		EqualityOperator result = null;
		if (operator.equals(InfixExpression.Operator.EQUALS)) {
			result = operatorsFactory.createEqual();
		} else if (operator.equals(InfixExpression.Operator.NOT_EQUALS)) {
			result = operatorsFactory.createNotEqual();
		}
		return result;
	}

}
