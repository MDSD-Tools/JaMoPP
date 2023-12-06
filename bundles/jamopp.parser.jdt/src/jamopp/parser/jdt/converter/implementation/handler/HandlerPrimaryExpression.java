package jamopp.parser.jdt.converter.implementation.handler;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.PrimaryExpression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerPrimaryExpression implements ExpressionHandler {

	private final ToConverter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(
			ToConverter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	public Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}
