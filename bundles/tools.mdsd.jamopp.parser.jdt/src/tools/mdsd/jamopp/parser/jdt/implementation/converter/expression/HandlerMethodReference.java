package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;

public class HandlerMethodReference implements ExpressionHandler {

	private final Converter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter;

	@Inject
	HandlerMethodReference(
			Converter<MethodReference, MethodReferenceExpression> toMethodReferenceExpressionConverter) {
		this.toMethodReferenceExpressionConverter = toMethodReferenceExpressionConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		return toMethodReferenceExpressionConverter.convert((MethodReference) expr);
	}

}
