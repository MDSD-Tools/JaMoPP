package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;

public class HandlerConditionalExpression implements ExpressionHandler {

	private final Converter<ConditionalExpression, tools.mdsd.jamopp.model.java.expressions.ConditionalExpression> toConditionalExpressionConverter;

	@Inject
	public HandlerConditionalExpression(
			final Converter<ConditionalExpression, tools.mdsd.jamopp.model.java.expressions.ConditionalExpression> toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(final Expression expr) {
		return toConditionalExpressionConverter.convert((ConditionalExpression) expr);
	}

}
