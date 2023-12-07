package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;

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
