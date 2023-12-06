package jamopp.parser.jdt.converter.handler;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ExpressionHandler;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class HandlerConditionalExpression implements ExpressionHandler {

	private final ToConverter<org.eclipse.jdt.core.dom.ConditionalExpression, org.emftext.language.java.expressions.ConditionalExpression> toConditionalExpressionConverter;

	@Inject
	HandlerConditionalExpression(
			ToConverter<org.eclipse.jdt.core.dom.ConditionalExpression, org.emftext.language.java.expressions.ConditionalExpression> toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toConditionalExpressionConverter.convert((ConditionalExpression) expr);
	}

}
