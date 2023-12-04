package jamopp.parser.jdt.converter.helper.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.ToMethodReferenceExpressionConverter;

public class HandlerMethodReference implements ExpressionHandler {

	private final ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
