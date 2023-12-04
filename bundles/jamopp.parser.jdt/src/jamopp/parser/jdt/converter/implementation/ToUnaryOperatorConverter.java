package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToUnaryOperatorConverter implements ToConverter<PrefixExpression.Operator, UnaryOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToUnaryOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;

	}

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
