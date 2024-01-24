package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.PrimaryExpression;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;

public class HandlerPrimaryExpression implements ExpressionHandler {

	private final Converter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter;

	@Inject
	public HandlerPrimaryExpression(
			final Converter<org.eclipse.jdt.core.dom.Expression, PrimaryExpression> toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
	}

	@Override
	public Expression handle(final org.eclipse.jdt.core.dom.Expression expr) {
		return toPrimaryExpressionConverter.convert(expr);
	}

}
