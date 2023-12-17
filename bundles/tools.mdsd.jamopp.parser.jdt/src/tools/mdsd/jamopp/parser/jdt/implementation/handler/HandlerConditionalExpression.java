package tools.mdsd.jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.handler.ExpressionHandler;

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
