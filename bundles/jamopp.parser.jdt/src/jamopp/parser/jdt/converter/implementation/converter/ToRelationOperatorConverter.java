package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.OperatorsFactory;
import org.emftext.language.java.operators.RelationOperator;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToRelationOperatorConverter implements ToConverter<InfixExpression.Operator, RelationOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToRelationOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	public RelationOperator convert(InfixExpression.Operator op) {
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
