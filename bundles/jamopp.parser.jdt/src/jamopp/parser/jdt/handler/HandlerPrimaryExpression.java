package jamopp.parser.jdt.handler;

import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.parser.jdt.other.ToPrimaryExpressionConverter;

public class HandlerPrimaryExpression extends Handler {

	private final ToPrimaryExpressionConverter toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(ToPrimaryExpressionConverter toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	public
	Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}
