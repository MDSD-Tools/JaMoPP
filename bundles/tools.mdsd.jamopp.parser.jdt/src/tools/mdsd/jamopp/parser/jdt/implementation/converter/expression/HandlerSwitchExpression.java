package tools.mdsd.jamopp.parser.jdt.implementation.converter.expression;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ExpressionHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToSwitchCasesAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class HandlerSwitchExpression implements ExpressionHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout utilLayout;
	private final ToSwitchCasesAndSetConverter utilStatementConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerSwitchExpression(ToSwitchCasesAndSetConverter utilStatementConverter, UtilLayout utilLayout,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> toExpressionConverter,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.expressions.Expression handle(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		tools.mdsd.jamopp.model.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(toExpressionConverter.convert(switchExpr.getExpression()));
		utilStatementConverter.convert(result, switchExpr.statements());
		utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
