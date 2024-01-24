package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.InfixExpression;

import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToAdditiveOperatorConverter implements Converter<InfixExpression.Operator, AdditiveOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToAdditiveOperatorConverter(final OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public AdditiveOperator convert(final InfixExpression.Operator operator) {
		AdditiveOperator result = null;
		if (operator.equals(InfixExpression.Operator.PLUS)) {
			result = operatorsFactory.createAddition();
		} else if (operator.equals(InfixExpression.Operator.MINUS)) {
			result = operatorsFactory.createSubtraction();
		}
		return result;
	}

}
