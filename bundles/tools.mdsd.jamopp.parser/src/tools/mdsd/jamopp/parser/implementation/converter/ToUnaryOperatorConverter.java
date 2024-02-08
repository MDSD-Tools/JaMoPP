package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.PrefixExpression;

import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToUnaryOperatorConverter implements Converter<PrefixExpression.Operator, UnaryOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToUnaryOperatorConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;

	}

	@Override
	public UnaryOperator convert(final PrefixExpression.Operator operator) {
		UnaryOperator result = null;
		if (operator.equals(PrefixExpression.Operator.COMPLEMENT)) {
			result = operatorsFactory.createComplement();
		} else if (operator.equals(PrefixExpression.Operator.NOT)) {
			result = operatorsFactory.createNegate();
		} else if (operator.equals(PrefixExpression.Operator.PLUS)) {
			result = operatorsFactory.createAddition();
		} else if (operator.equals(PrefixExpression.Operator.MINUS)) {
			result = operatorsFactory.createSubtraction();
		}
		return result;
	}

}
