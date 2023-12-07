package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerConditionalExpression implements ExpressionHandler {

	private final Converter<org.eclipse.jdt.core.dom.ConditionalExpression, org.emftext.language.java.expressions.ConditionalExpression> toConditionalExpressionConverter;

	@Inject
	HandlerConditionalExpression(
			Converter<org.eclipse.jdt.core.dom.ConditionalExpression, org.emftext.language.java.expressions.ConditionalExpression> toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toConditionalExpressionConverter.convert((ConditionalExpression) expr);
	}

}
