package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;

public class HandlerConditionalExpression implements ExpressionHandler {

	private final Converter<org.eclipse.jdt.core.dom.ConditionalExpression, tools.mdsd.jamopp.model.java.expressions.ConditionalExpression> toConditionalExpressionConverter;

	@Inject
	HandlerConditionalExpression(
			Converter<org.eclipse.jdt.core.dom.ConditionalExpression, tools.mdsd.jamopp.model.java.expressions.ConditionalExpression> toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		return toConditionalExpressionConverter.convert((ConditionalExpression) expr);
	}

}
