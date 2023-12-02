package jamopp.parser.jdt;

import org.emftext.language.java.expressions.Expression;

public class HandlerPrimaryExpression extends Handler {

	private final ToPrimaryExpressionConverter toPrimaryExpressionConverter;

	public HandlerPrimaryExpression(ToPrimaryExpressionConverter toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convertToPrimaryExpression(expr);
	}

}
