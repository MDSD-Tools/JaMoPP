package tools.mdsd.jamopp.parser.implementation.converter.expression;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.Provider;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;

public class ToExpressionConverterImpl
		implements Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> {

	private final Provider<ExpressionHandler> handlerPrimaryExpression;
	private final Provider<ExpressionHandler> handlerMethodReference;
	private final Map<Integer, Provider<ExpressionHandler>> mapping;

	@Inject
	public ToExpressionConverterImpl(
			@Named("HandlerSwitchExpression") final Provider<ExpressionHandler> handlerSwitchExpression,
			@Named("HandlerPrimaryExpression") final Provider<ExpressionHandler> handlerPrimaryExpression,
			@Named("HandlerPrefixExpression") final Provider<ExpressionHandler> handlerPrefixExpression,
			@Named("HandlerPostfixExpression") final Provider<ExpressionHandler> handlerPostfixExpression,
			@Named("HandlerMethodReference") final Provider<ExpressionHandler> handlerMethodReference,
			@Named("HandlerLambdaExpression") final Provider<ExpressionHandler> handlerLambdaExpression,
			@Named("HandlerInstanceOf") final Provider<ExpressionHandler> handlerInstanceOf,
			@Named("HandlerInfixExpression") final Provider<ExpressionHandler> handlerInfixExpression,
			@Named("HandlerConditionalExpression") final Provider<ExpressionHandler> handlerConditionalExpression,
			@Named("HandlerCastExpression") final Provider<ExpressionHandler> handlerCastExpression,
			@Named("HandlerAssignment") final Provider<ExpressionHandler> handlerAssignment) {
		this.handlerPrimaryExpression = handlerPrimaryExpression;
		this.handlerMethodReference = handlerMethodReference;
		mapping = new HashMap<>();
		mapping.put(ASTNode.ASSIGNMENT, handlerAssignment);
		mapping.put(ASTNode.CONDITIONAL_EXPRESSION, handlerConditionalExpression);
		mapping.put(ASTNode.INFIX_EXPRESSION, handlerInfixExpression);
		mapping.put(ASTNode.INSTANCEOF_EXPRESSION, handlerInstanceOf);
		mapping.put(ASTNode.PREFIX_EXPRESSION, handlerPrefixExpression);
		mapping.put(ASTNode.POSTFIX_EXPRESSION, handlerPostfixExpression);
		mapping.put(ASTNode.CAST_EXPRESSION, handlerCastExpression);
		mapping.put(ASTNode.SWITCH_EXPRESSION, handlerSwitchExpression);
		mapping.put(ASTNode.LAMBDA_EXPRESSION, handlerLambdaExpression);
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression convert(final Expression expr) {
		tools.mdsd.jamopp.model.java.expressions.Expression result;
		if (expr instanceof MethodReference) {
			result = handlerMethodReference.get().handle(expr);
		} else {
			result = mapping.getOrDefault(expr.getNodeType(), handlerPrimaryExpression).get().handle(expr);
		}
		return result;
	}

}
