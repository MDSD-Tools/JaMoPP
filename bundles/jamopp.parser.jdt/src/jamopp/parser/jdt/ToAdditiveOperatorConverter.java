package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;

public class ToAdditiveOperatorConverter {

	org.emftext.language.java.operators.AdditiveOperator convertToAdditiveOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.PLUS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAddition();
		}
		if (op == InfixExpression.Operator.MINUS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createSubtraction();
		}
		return null;
	}
	
}
