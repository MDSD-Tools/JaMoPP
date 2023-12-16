package jamopp.parser.jdt.implementation.handler;

import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.PrimaryExpression;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.handler.ExpressionHandler;

public class HandlerPrimaryExpression implements ExpressionHandler {

	private final Converter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(
			Converter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	public Expression handle(org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}
