package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.OperatorsFactory;

class ToRelationOperatorConverter {
	
	private final OperatorsFactory operatorsFactory;
	
	public ToRelationOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}
	
	org.emftext.language.java.operators.RelationOperator convertToRelationOperator(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.GREATER) {
			return operatorsFactory.createGreaterThan();
		}
		if (op == InfixExpression.Operator.GREATER_EQUALS) {
			return operatorsFactory.createGreaterThanOrEqual();
		}
		if (op == InfixExpression.Operator.LESS) {
			return operatorsFactory.createLessThan();
		}
		if (op == InfixExpression.Operator.LESS_EQUALS) {
			return operatorsFactory.createLessThanOrEqual();
		}
		return null;
	}

}
