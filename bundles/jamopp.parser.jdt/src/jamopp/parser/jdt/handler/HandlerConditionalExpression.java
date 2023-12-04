package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToConditionalExpressionConverter;

public class HandlerConditionalExpression extends Handler {

	private final ToConditionalExpressionConverter toConditionalExpressionConverter;

	@Inject
	HandlerConditionalExpression(ToConditionalExpressionConverter toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toConditionalExpressionConverter.convert((ConditionalExpression) expr);
	}

}
