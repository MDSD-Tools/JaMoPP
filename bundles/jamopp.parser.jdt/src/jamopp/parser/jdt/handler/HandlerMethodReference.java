package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToMethodReferenceExpressionConverter;

public class HandlerMethodReference extends Handler {

	private final ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
