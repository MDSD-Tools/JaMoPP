package jamopp.parser.jdt.converter.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.emftext.language.java.expressions.MethodReferenceExpression;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ExpressionHandler;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class HandlerMethodReference implements ExpressionHandler {

	private final ToConverter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(
			ToConverter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
