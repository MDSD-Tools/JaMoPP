package jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import jamopp.parser.jdt.converter.implementation.handler.HandlerAssignment;
import jamopp.parser.jdt.converter.implementation.handler.HandlerCastExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerConditionalExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerInfixExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerInstanceOf;
import jamopp.parser.jdt.converter.implementation.handler.HandlerLambdaExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerMethodReference;
import jamopp.parser.jdt.converter.implementation.handler.HandlerPostfixExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerPrefixExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerPrimaryExpression;
import jamopp.parser.jdt.converter.implementation.handler.HandlerSwitchExpression;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

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
