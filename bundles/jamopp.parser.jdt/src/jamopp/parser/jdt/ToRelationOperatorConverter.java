package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;

public class ToRelationOperatorConverter {
	
	org.emftext.language.java.operators.RelationOperator convertToRelationOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.GREATER) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createGreaterThan();
		}
		if (op == InfixExpression.Operator.GREATER_EQUALS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createGreaterThanOrEqual();
		}
		if (op == InfixExpression.Operator.LESS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createLessThan();
		}
		if (op == InfixExpression.Operator.LESS_EQUALS) {
			return org.emftext.language.java.operators.OperatorsFactory.eINSTANCE.createLessThanOrEqual();
		}
		return null;
	}

}
