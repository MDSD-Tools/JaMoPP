package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToMultiplicativeOperatorConverter implements Converter<InfixExpression.Operator, MultiplicativeOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToMultiplicativeOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public MultiplicativeOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.TIMES) {
			return operatorsFactory.createMultiplication();
		}
		if (op == InfixExpression.Operator.DIVIDE) {
			return operatorsFactory.createDivision();
		}
		if (op == InfixExpression.Operator.REMAINDER) {
			return operatorsFactory.createRemainder();
		}
		return null;
	}

}
