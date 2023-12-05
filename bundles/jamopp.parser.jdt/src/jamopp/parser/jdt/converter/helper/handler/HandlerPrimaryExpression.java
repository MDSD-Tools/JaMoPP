package jamopp.parser.jdt.converter.helper.handler;

import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToPrimaryExpressionConverter;

public class HandlerPrimaryExpression implements ExpressionHandler {

	private final ToPrimaryExpressionConverter toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(ToPrimaryExpressionConverter toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	public Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}