package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;

public class ToMultiplicativeOperatorConverter {
	
	org.emftext.language.java.operators.MultiplicativeOperator convertToMultiplicativeOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.TIMES) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createMultiplication();
		}
		if (op == InfixExpression.Operator.DIVIDE) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createDivision();
		}
		if (op == InfixExpression.Operator.REMAINDER) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createRemainder();
		}
		return null;
	}

}
