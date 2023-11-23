package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.OperatorsFactory;

public class ToEqualityOperatorConverter {

	private static final OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;

	EqualityOperator convertToEqualityOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.EQUALS) {
			return operatorsFactory.createEqual();
		}
		if (op == InfixExpression.Operator.NOT_EQUALS) {
			return operatorsFactory.createNotEqual();
		}
		return null;
	}

}
