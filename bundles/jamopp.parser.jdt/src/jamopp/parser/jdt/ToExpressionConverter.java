package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;
import com.google.inject.Inject;
import com.google.inject.Provider;


class ToExpressionConverter {

	private final Provider<ToPrimaryExpressionConverter> toPrimaryExpressionConverter;
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
	ToExpressionConverter(Provider<HandlerSwitchExpression> handlerSwitchExpression,
			Provider<HandlerPrefixExpression> handlerPrefixExpression,
			Provider<HandlerPostfixExpression> handlerPostfixExpression,
			Provider<HandlerMethodReference> handlerMethodReference,
			Provider<HandlerLambdaExpression> handlerLambdaExpression, Provider<HandlerInstanceOf> handlerInstanceOf,
			Provider<HandlerInfixExpression> handlerInfixExpression,
			Provider<HandlerConditionalExpression> handlerConditionalExpression,
			Provider<HandlerCastExpression> handlerCastExpression, Provider<HandlerAssignment> handlerAssignment,
			Provider<ToPrimaryExpressionConverter> toPrimaryExpressionConverter) {
		this.toPrimaryExpressionConverter = toPrimaryExpressionConverter;
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

	org.emftext.language.java.expressions.Expression convertToExpression(Expression expr) {
		if (expr.getNodeType() == ASTNode.ASSIGNMENT) {
			return handlerAssignment.get().handleAssignment(expr);
		} else if (expr.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
			return handlerConditionalExpression.get().handleConditionalExpression(expr);
		} else if (expr.getNodeType() == ASTNode.INFIX_EXPRESSION) {
			return handlerInfixExpression.get().handleInfixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
			return handlerInstanceOf.get().handleInstanceOf(expr);
		} else if (expr.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
			return handlerPrefixExpression.get().handlePrefixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
			return handlerPostfixExpression.get().handlePostfixExpression(expr);
		} else if (expr.getNodeType() == ASTNode.CAST_EXPRESSION) {
			return handlerCastExpression.get().handleCastExpression(expr);
		} else if (expr.getNodeType() == ASTNode.SWITCH_EXPRESSION) {
			return handlerSwitchExpression.get().handleSwitchExpression(expr);
		} else if (expr instanceof MethodReference) {
			return handlerMethodReference.get().handleMethodReference(expr);
		} else if (expr.getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
			return handlerLambdaExpression.get().handleLambdaExpression(expr);
		} else {
			return toPrimaryExpressionConverter.get().convertToPrimaryExpression(expr);
		}
	}

}
