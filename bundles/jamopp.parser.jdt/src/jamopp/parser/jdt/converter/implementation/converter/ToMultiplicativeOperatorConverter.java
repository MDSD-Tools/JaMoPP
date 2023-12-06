package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.MultiplicativeOperator;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToMultiplicativeOperatorConverter implements ToConverter<InfixExpression.Operator, MultiplicativeOperator> {

	private final OperatorsFactory operatorsFactory;

	@Inject
	public ToMultiplicativeOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

	@Override
	public MultiplicativeOperator convert(InfixExpression.Operator op) {
		if (op == InfixExpression.Operator.TIMES) {
			return operatorsFactory.createMultiplication();
		}
		if (op == InfixExpression.Operator.DIVIDE) {
			return operatorsFactory.createDivision();
		}
		if (op == InfixExpression.Operator.REMAINDER) {
			return operatorsFactory.createRemainder();
		}
		return null;
	}

}
