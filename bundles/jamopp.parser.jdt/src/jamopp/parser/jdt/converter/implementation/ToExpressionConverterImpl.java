package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.handler.ExpressionHandler;
import jamopp.parser.jdt.converter.helper.handler.HandlerAssignment;
import jamopp.parser.jdt.converter.helper.handler.HandlerCastExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerConditionalExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerInfixExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerInstanceOf;
import jamopp.parser.jdt.converter.helper.handler.HandlerLambdaExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerMethodReference;
import jamopp.parser.jdt.converter.helper.handler.HandlerPostfixExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerPrefixExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerPrimaryExpression;
import jamopp.parser.jdt.converter.helper.handler.HandlerSwitchExpression;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

public class ToExpressionConverterImpl
		implements ToExpressionConverter, ToConverter<Expression, org.emftext.language.java.expressions.Expression> {

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
	public void setHandlerPrimaryExpression(HandlerPrimaryExpression handlerPrimaryExpression) {
		this.handlerPrimaryExpression = handlerPrimaryExpression;
	}

	@Inject
	public void setHandlerAssignment(HandlerAssignment handlerAssignment) {
		this.handlerAssignment = handlerAssignment;
	}

	@Inject
	public void setHandlerConditionalExpression(HandlerConditionalExpression handlerConditionalExpression) {
		this.handlerConditionalExpression = handlerConditionalExpression;
	}

	@Inject
	public void setHandlerInfixExpression(HandlerInfixExpression handlerInfixExpression) {
		this.handlerInfixExpression = handlerInfixExpression;
	}

	@Inject
	public void setHandlerInstanceOf(HandlerInstanceOf handlerInstanceOf) {
		this.handlerInstanceOf = handlerInstanceOf;
	}

	@Inject
	public void setHandlerPrefixExpression(HandlerPrefixExpression handlerPrefixExpression) {
		this.handlerPrefixExpression = handlerPrefixExpression;
	}

	@Inject
	public void setHandlerPostfixExpression(HandlerPostfixExpression handlerPostfixExpression) {
		this.handlerPostfixExpression = handlerPostfixExpression;
	}

	@Inject
	public void setHandlerCastExpression(HandlerCastExpression handlerCastExpression) {
		this.handlerCastExpression = handlerCastExpression;
	}

	@Inject
	public void setHandlerSwitchExpression(HandlerSwitchExpression handlerSwitchExpression) {
		this.handlerSwitchExpression = handlerSwitchExpression;
	}

	@Inject
	public void setHandlerMethodReference(HandlerMethodReference handlerMethodReference) {
		this.handlerMethodReference = handlerMethodReference;
	}

	@Inject
	public void setHandlerLambdaExpression(HandlerLambdaExpression handlerLambdaExpression) {
		this.handlerLambdaExpression = handlerLambdaExpression;
	}

}
