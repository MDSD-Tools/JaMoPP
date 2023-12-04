package jamopp.parser.jdt.converter.other;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.emftext.language.java.operators.AdditiveOperator;
import org.emftext.language.java.operators.OperatorsFactory;

import com.google.inject.Inject;

public class ToAdditiveOperatorConverter {

	private final OperatorsFactory operatorsFactory;

	@Inject
	ToAdditiveOperatorConverter(OperatorsFactory operatorsFactory) {
		this.operatorsFactory = operatorsFactory;
	}

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
