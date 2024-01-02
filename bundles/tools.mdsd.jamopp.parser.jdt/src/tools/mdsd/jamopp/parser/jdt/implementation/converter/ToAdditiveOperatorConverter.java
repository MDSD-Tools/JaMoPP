package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;
import tools.mdsd.jamopp.model.java.operators.OperatorsFactory;

import javax.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToAdditiveOperatorConverter implements Converter<InfixExpression.Operator, AdditiveOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToAdditiveOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public AdditiveOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.PLUS) {
			return operatorsFactory.createAddition();
		}
		if (op == InfixExpression.Operator.MINUS) {
			return operatorsFactory.createSubtraction();
		}
		return null;
	}

}
