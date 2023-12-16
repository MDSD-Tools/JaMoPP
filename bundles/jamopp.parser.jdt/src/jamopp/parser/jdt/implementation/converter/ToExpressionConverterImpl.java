package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.handler.ExpressionHandler;

public class ToExpressionConverterImpl
		implements Converter<Expression, org.emftext.language.java.expressions.Expression> {

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
	}

	@Override
	public org.emftext.language.java.expressions.Expression convert(Expression expr) {
		if (expr.getNodeType() == ASTNode.ASSIGNMENT) {
			return handlerAssignment.handle(expr);
		} else if (expr.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION) {
			return handlerConditionalExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.INFIX_EXPRESSION) {
			return handlerInfixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.INSTANCEOF_EXPRESSION) {
			return handlerInstanceOf.handle(expr);
		} else if (expr.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
			return handlerPrefixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
			return handlerPostfixExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.CAST_EXPRESSION) {
			return handlerCastExpression.handle(expr);
		} else if (expr.getNodeType() == ASTNode.SWITCH_EXPRESSION) {
			return handlerSwitchExpression.handle(expr);
		} else if (expr instanceof MethodReference) {
			return handlerMethodReference.handle(expr);
		} else if (expr.getNodeType() == ASTNode.LAMBDA_EXPRESSION) {
			return handlerLambdaExpression.handle(expr);
		} else {
			return handlerPrimaryExpression.handle(expr);
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
