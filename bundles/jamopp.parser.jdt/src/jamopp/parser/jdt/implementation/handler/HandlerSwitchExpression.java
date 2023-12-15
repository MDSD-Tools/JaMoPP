package jamopp.parser.jdt.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.handler.ExpressionHandler;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;

public class HandlerSwitchExpression implements ExpressionHandler {

	private final StatementsFactory statementsFactory;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;
	private final UtilLayout utilLayout;
	private final UtilToSwitchCasesAndSetConverter utilStatementConverter;

	@Inject
	HandlerSwitchExpression(UtilToSwitchCasesAndSetConverter utilStatementConverter, UtilLayout utilLayout,
			Converter<Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		var switchExpr = (SwitchExpression) expr;
		var result = this.statementsFactory.createSwitch();
		result.setVariable(this.toExpressionConverter.convert(switchExpr.getExpression()));
		this.utilStatementConverter.convert(result, switchExpr.statements());
		this.utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
