package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.emftext.language.java.expressions.ExpressionsFactory;

import com.google.inject.Inject;

class HandlerMethodReference {

	private final ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(ToMethodReferenceExpressionConverter toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	org.emftext.language.java.expressions.Expression handleMethodReference(Expression expr) {
		return toMethodReferenceExpressionConverter.convertToMethodReferenceExpression((MethodReference) expr);
	}

}
