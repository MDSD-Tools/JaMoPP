package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import javax.inject.Inject;
import javax.inject.Named;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;

public class ToExpressionConverterImpl
		implements Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> {

	private ExpressionHandler handlerPrimaryExpression;
	private ExpressionHandler handlerAssignment;
	private ExpressionHandler handlerConditionalExpression;
	private ExpressionHandler handlerInfixExpression;
	private ExpressionHandler handlerInstanceOf;
	private ExpressionHandler handlerPrefixExpression;
	private ExpressionHandler handlerPostfixExpression;
	private ExpressionHandler handlerCastExpression;
	private ExpressionHandler handlerSwitchExpression;
	private ExpressionHandler handlerMethodReference;
	private ExpressionHandler handlerLambdaExpression;

	@Inject
	ToExpressionConverterImpl() {
		// Injection is handled by setter
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression convert(Expression expr) {
		if (expr.getNodeType() == ASTNode.ASSIGNMENT) {
			return this.handlerAssignment.handle(expr);
		}
		if (expr.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
			return this.handlerConditionalExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.INFIX_EXPRESSION) {
			return this.handlerInfixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
			return this.handlerInstanceOf.handle(expr);
		} else if (expr.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
			return this.handlerPrefixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
			return this.handlerPostfixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.CAST_EXPRESSION) {
			return this.handlerCastExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.SWITCH_EXPRESSION) {
			return this.handlerSwitchExpression.handle(expr);
		} else if (expr instanceof MethodReference) {
			return this.handlerMethodReference.handle(expr);
		} else if (expr.getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
			return this.handlerLambdaExpression.handle(expr);
		} else {
			return this.handlerPrimaryExpression.handle(expr);
		}
	}

	@Inject
	public void setHandlerPrimaryExpression(
			@Named("HandlerPrimaryExpression") ExpressionHandler handlerPrimaryExpression) {
		this.handlerPrimaryExpression = handlerPrimaryExpression;
	}

	@Inject
	public void setHandlerAssignment(@Named("HandlerAssignment") ExpressionHandler handlerAssignment) {
		this.handlerAssignment = handlerAssignment;
	}

	@Inject
	public void setHandlerConditionalExpression(
			@Named("HandlerConditionalExpression") ExpressionHandler handlerConditionalExpression) {
		this.handlerConditionalExpression = handlerConditionalExpression;
	}

	@Inject
	public void setHandlerInfixExpression(@Named("HandlerInfixExpression") ExpressionHandler handlerInfixExpression) {
		this.handlerInfixExpression = handlerInfixExpression;
	}

	@Inject
	public void setHandlerInstanceOf(@Named("HandlerInstanceOf") ExpressionHandler handlerInstanceOf) {
		this.handlerInstanceOf = handlerInstanceOf;
	}

	@Inject
	public void setHandlerPrefixExpression(
			@Named("HandlerPrefixExpression") ExpressionHandler handlerPrefixExpression) {
		this.handlerPrefixExpression = handlerPrefixExpression;
	}

	@Inject
	public void setHandlerPostfixExpression(
			@Named("HandlerPostfixExpression") ExpressionHandler handlerPostfixExpression) {
		this.handlerPostfixExpression = handlerPostfixExpression;
	}

	@Inject
	public void setHandlerCastExpression(@Named("HandlerCastExpression") ExpressionHandler handlerCastExpression) {
		this.handlerCastExpression = handlerCastExpression;
	}

	@Inject
	public void setHandlerSwitchExpression(
			@Named("HandlerSwitchExpression") ExpressionHandler handlerSwitchExpression) {
		this.handlerSwitchExpression = handlerSwitchExpression;
	}

	@Inject
	public void setHandlerMethodReference(@Named("HandlerMethodReference") ExpressionHandler handlerMethodReference) {
		this.handlerMethodReference = handlerMethodReference;
	}

	@Inject
	public void setHandlerLambdaExpression(
			@Named("HandlerLambdaExpression") ExpressionHandler handlerLambdaExpression) {
		this.handlerLambdaExpression = handlerLambdaExpression;
	}

}
