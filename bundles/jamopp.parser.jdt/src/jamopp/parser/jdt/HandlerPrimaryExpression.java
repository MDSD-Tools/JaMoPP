package jamopp.parser.jdt;

import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

public class HandlerPrimaryExpression extends Handler {

	private final ToPrimaryExpressionConverter toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(ToPrimaryExpressionConverter toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}
