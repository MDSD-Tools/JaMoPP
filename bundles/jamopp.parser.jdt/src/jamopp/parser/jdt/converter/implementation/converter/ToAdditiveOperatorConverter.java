package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.AdditiveOperator;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;

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
