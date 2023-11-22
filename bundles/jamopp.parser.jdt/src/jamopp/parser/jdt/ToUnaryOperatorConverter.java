package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.PrefixExpression;

public class ToUnaryOperatorConverter {

	org.emftext.language.java.operators.UnaryOperator convertToUnaryOperator(PrefixExpression.Operator op) {
		if (op == PrefixExpression.Operator.COMPLEMENT) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createComplement();
		}
		if (op == PrefixExpression.Operator.NOT) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createNegate();
		}
		if (op == PrefixExpression.Operator.PLUS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createAddition();
		}
		if (op == PrefixExpression.Operator.MINUS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createSubtraction();
		}
		return null;
	}

}
