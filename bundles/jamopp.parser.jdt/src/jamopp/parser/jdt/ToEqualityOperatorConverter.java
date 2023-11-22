package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;

public class ToEqualityOperatorConverter {

	org.emftext.language.java.operators.EqualityOperator convertToEqualityOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.EQUALS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createEqual();
		}
		if (op == InfixExpression.Operator.NOT_EQUALS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createNotEqual();
		}
		return null;
	}
	
}
