package tools.mdsd.jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerAssignment;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerCastExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerConditionalExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerInfixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerInstanceOf;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerLambdaExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerMethodReference;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerPostfixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerPrefixExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerPrimaryExpression;
import tools.mdsd.jamopp.parser.jdt.implementation.handler.HandlerSwitchExpression;
import tools.mdsd.jamopp.parser.jdt.interfaces.handler.ExpressionHandler;

public class HandlerModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerAssignment")).to(HandlerAssignment.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerCastExpression")).to(HandlerCastExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerConditionalExpression")).to(HandlerConditionalExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerInfixExpression")).to(HandlerInfixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerInstanceOf")).to(HandlerInstanceOf.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerLambdaExpression")).to(HandlerLambdaExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerMethodReference")).to(HandlerMethodReference.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPostfixExpression")).to(HandlerPostfixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPrefixExpression")).to(HandlerPrefixExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerPrimaryExpression")).to(HandlerPrimaryExpression.class);
		bind(ExpressionHandler.class).annotatedWith(Names.named("HandlerSwitchExpression")).to(HandlerSwitchExpression.class);

	}

}