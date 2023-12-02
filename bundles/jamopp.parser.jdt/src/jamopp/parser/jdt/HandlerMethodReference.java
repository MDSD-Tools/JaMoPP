package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class HandlerMethodReference extends Handler {

	private final ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
