package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

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

	private final Map<Integer, ExpressionHandler> mapping;

	@Inject
	ToExpressionConverterImpl() {
		mapping = new HashMap<>();
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression convert(Expression expr) {

		if (mapping.isEmpty()) {
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

		if (expr instanceof MethodReference) {
			return handlerMethodReference.handle(expr);
		}

		return mapping.getOrDefault(expr.getNodeType(), handlerPrimaryExpression).handle(expr);
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
