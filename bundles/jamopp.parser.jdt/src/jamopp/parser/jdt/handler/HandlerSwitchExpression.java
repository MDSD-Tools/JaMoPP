package jamopp.parser.jdt.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class HandlerSwitchExpression extends Handler {

	private final StatementsFactory statementsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;
	private final UtilToSwitchCasesAndSetConverter utilStatementConverter;

	@Inject
	HandlerSwitchExpression(UtilToSwitchCasesAndSetConverter utilStatementConverter, UtilLayout utilLayout,
			ToExpressionConverter toExpressionConverter, StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	@Override
	public
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		org.emftext.language.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(toExpressionConverter.convert(switchExpr.getExpression()));
		utilStatementConverter.convert(result, switchExpr.statements());
		utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
