package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.PrefixExpression;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToUnaryOperatorConverter implements Converter<PrefixExpression.Operator, UnaryOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToUnaryOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;

	}

	@Override
	public UnaryOperator convert(PrefixExpression.Operator op) {
		if (op == PrefixExpression.Operator.COMPLEMENT) {
			return operatorsFactory.createComplement();
		}
		if (op == PrefixExpression.Operator.NOT) {
			return operatorsFactory.createNegate();
		}
		if (op == PrefixExpression.Operator.PLUS) {
			return operatorsFactory.createAddition();
		}
		if (op == PrefixExpression.Operator.MINUS) {
			return operatorsFactory.createSubtraction();
		}
		return null;
	}

}
