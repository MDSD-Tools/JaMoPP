package tools.mdsd.jamopp.parser.implementation.converter.expression;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;

public class HandlerMethodReference implements ExpressionHandler {

	private final Converter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter;

	@Inject
	public HandlerMethodReference(
			final Converter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(final Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
