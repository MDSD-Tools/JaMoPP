package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

class ToMultiplicativeOperatorConverter {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToMultiplicativeOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	org.emftext.language.java.operators.MultiplicativeOperator convertToMultiplicativeOperator(
			InfixExpression.Operator op) {
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
