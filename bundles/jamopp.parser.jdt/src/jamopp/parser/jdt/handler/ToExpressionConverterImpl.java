package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

public class ToExpressionConverterImpl implements ToExpressionConverter {

	private final Provider<HandlerPrimaryExpression> handlerPrimaryExpression;
	private final Provider<HandlerAssignment> handlerAssignment;
	private final Provider<HandlerConditionalExpression> handlerConditionalExpression;
	private final Provider<HandlerInfixExpression> handlerInfixExpression;
	private final Provider<HandlerInstanceOf> handlerInstanceOf;
	private final Provider<HandlerPrefixExpression> handlerPrefixExpression;
	private final Provider<HandlerPostfixExpression> handlerPostfixExpression;
	private final Provider<HandlerCastExpression> handlerCastExpression;
	private final Provider<HandlerSwitchExpression> handlerSwitchExpression;
	private final Provider<HandlerMethodReference> handlerMethodReference;
	private final Provider<HandlerLambdaExpression> handlerLambdaExpression;

	@Inject
	ToExpressionConverterImpl(Provider<HandlerSwitchExpression> handlerSwitchExpression,
			Provider<HandlerPrefixExpression> handlerPrefixExpression,
			Provider<HandlerPostfixExpression> handlerPostfixExpression,
			Provider<HandlerMethodReference> handlerMethodReference,
			Provider<HandlerLambdaExpression> handlerLambdaExpression, Provider<HandlerInstanceOf> handlerInstanceOf,
			Provider<HandlerInfixExpression> handlerInfixExpression,
			Provider<HandlerConditionalExpression> handlerConditionalExpression,
			Provider<HandlerCastExpression> handlerCastExpression, Provider<HandlerAssignment> handlerAssignment,
			Provider<HandlerPrimaryExpression> toPrimaryExpressionConverter) {
		this.handlerPrimaryExpression = toPrimaryExpressionConverter;
		this.handlerAssignment = handlerAssignment;
		this.handlerConditionalExpression = handlerConditionalExpression;
		this.handlerInfixExpression = handlerInfixExpression;
		this.handlerInstanceOf = handlerInstanceOf;
		this.handlerPrefixExpression = handlerPrefixExpression;
		this.handlerPostfixExpression = handlerPostfixExpression;
		this.handlerCastExpression = handlerCastExpression;
		this.handlerSwitchExpression = handlerSwitchExpression;
		this.handlerMethodReference = handlerMethodReference;
		this.handlerLambdaExpression = handlerLambdaExpression;
	}

	@Override
	public org.emftext.language.java.expressions.Expression convert(Expression expr) {
		if (expr.getNodeType() == ASTNode.ASSIGNMENT) {
			return handlerAssignment.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
			return handlerConditionalExpression.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.INFIX_EXPRESSION) {
			return handlerInfixExpression.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
			return handlerInstanceOf.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
			return handlerPrefixExpression.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
			return handlerPostfixExpression.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.CAST_EXPRESSION) {
			return handlerCastExpression.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.SWITCH_EXPRESSION) {
			return handlerSwitchExpression.get().handle(expr);
		} else if (expr instanceof MethodReference) {
			return handlerMethodReference.get().handle(expr);
		} else if (expr.getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
			return handlerLambdaExpression.get().handle(expr);
		} else {
			return handlerPrimaryExpression.get().handle(expr);
		}
	}

}
