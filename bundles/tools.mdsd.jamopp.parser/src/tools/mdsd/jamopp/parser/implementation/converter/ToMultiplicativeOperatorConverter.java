package tools.mdsd.jamopp.parser.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToMultiplicativeOperatorConverter implements Converter<InfixExpression.Operator, MultiplicativeOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToMultiplicativeOperatorConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public MultiplicativeOperator convert(final InfixExpression.Operator operator) {
		MultiplicativeOperator result = null;
		if (operator.equals(InfixExpression.Operator.TIMES)) {
			result = operatorsFactory.createMultiplication();
		} else if (operator.equals(InfixExpression.Operator.DIVIDE)) {
			result = operatorsFactory.createDivision();
		} else if (operator.equals(InfixExpression.Operator.REMAINDER)) {
			result = operatorsFactory.createRemainder();
		}
		return result;
	}

}
