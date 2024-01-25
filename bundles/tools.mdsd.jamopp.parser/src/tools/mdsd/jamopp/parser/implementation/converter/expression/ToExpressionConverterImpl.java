package tools.mdsd.jamopp.parser.implementation.converter.expression;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodReference;

import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ExpressionHandler;

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
	public ToExpressionConverterImpl() {
		mapping = new HashMap<>();
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression convert(final Expression expr) {

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

		tools.mdsd.jamopp.model.java.expressions.Expression result;
		if (expr instanceof MethodReference) {
			result = handlerMethodReference.handle(expr);
		} else {
			result = mapping.getOrDefault(expr.getNodeType(), handlerPrimaryExpression).handle(expr);
		}
		return result;
	}

	@Inject
	public void setHandlerPrimaryExpression(
			@Named("HandlerPrimaryExpression") final ExpressionHandler handlerPrimaryExpression) {
		this.handlerPrimaryExpression = handlerPrimaryExpression;
	}

	@Inject
	public void setHandlerAssignment(@Named("HandlerAssignment") final ExpressionHandler handlerAssignment) {
		this.handlerAssignment = handlerAssignment;
	}

	@Inject
	public void setHandlerConditionalExpression(
			@Named("HandlerConditionalExpression") final ExpressionHandler handlerConditionalExpression) {
		this.handlerConditionalExpression = handlerConditionalExpression;
	}

	@Inject
	public void setHandlerInfixExpression(
			@Named("HandlerInfixExpression") final ExpressionHandler handlerInfixExpression) {
		this.handlerInfixExpression = handlerInfixExpression;
	}

	@Inject
	public void setHandlerInstanceOf(@Named("HandlerInstanceOf") final ExpressionHandler handlerInstanceOf) {
		this.handlerInstanceOf = handlerInstanceOf;
	}

	@Inject
	public void setHandlerPrefixExpression(
			@Named("HandlerPrefixExpression") final ExpressionHandler handlerPrefixExpression) {
		this.handlerPrefixExpression = handlerPrefixExpression;
	}

	@Inject
	public void setHandlerPostfixExpression(
			@Named("HandlerPostfixExpression") final ExpressionHandler handlerPostfixExpression) {
		this.handlerPostfixExpression = handlerPostfixExpression;
	}

	@Inject
	public void setHandlerCastExpression(
			@Named("HandlerCastExpression") final ExpressionHandler handlerCastExpression) {
		this.handlerCastExpression = handlerCastExpression;
	}

	@Inject
	public void setHandlerSwitchExpression(
			@Named("HandlerSwitchExpression") final ExpressionHandler handlerSwitchExpression) {
		this.handlerSwitchExpression = handlerSwitchExpression;
	}

	@Inject
	public void setHandlerMethodReference(
			@Named("HandlerMethodReference") final ExpressionHandler handlerMethodReference) {
		this.handlerMethodReference = handlerMethodReference;
	}

	@Inject
	public void setHandlerLambdaExpression(
			@Named("HandlerLambdaExpression") final ExpressionHandler handlerLambdaExpression) {
		this.handlerLambdaExpression = handlerLambdaExpression;
	}

}
