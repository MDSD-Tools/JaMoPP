package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToEqualityOperatorConverter implements Converter<InfixExpression.Operator, EqualityOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToEqualityOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public EqualityOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.EQUALS) {
			return operatorsFactory.createEqual();
		}
		if (op == InfixExpression.Operator.NOT_EQUALS) {
			return operatorsFactory.createNotEqual();
		}
		return null;
	}

}
