package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.AdditiveOperator;
import org.emftext.language.java.operators.OperatorsFactory;

public class ToAdditiveOperatorConverter {

	private static final OperatorsFactory operatorsFactory = OperatorsFactory.eINSTANCE;

	AdditiveOperator convertToAdditiveOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.PLUS) {
			return operatorsFactory.createAddition();
		}
		if (op == InfixExpression.Operator.MINUS) {
			return operatorsFactory.createSubtraction();
		}
		return null;
	}

}
