package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.PrefixExpression;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

class ToUnaryOperatorConverter {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToUnaryOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;

	}

	org.emftext.language.java.operators.UnaryOperator convertToUnaryOperator(PrefixExpression.Operator op) {
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
