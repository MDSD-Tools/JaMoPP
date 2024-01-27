package tools.mdsd.jamopp.parser.implementation.converter.expression;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;

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
