package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import com.google.inject.Inject;

class HandlerConditionalExpression extends Handler {

	private final ToConditionalExpressionConverter toConditionalExpressionConverter;

	@Inject
	HandlerConditionalExpression(ToConditionalExpressionConverter toConditionalExpressionConverter) {
		this.toConditionalExpressionConverter = toConditionalExpressionConverter;
	}

	@Override
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toConditionalExpressionConverter.convertToConditionalExpression((ConditionalExpression) expr);
	}

}
