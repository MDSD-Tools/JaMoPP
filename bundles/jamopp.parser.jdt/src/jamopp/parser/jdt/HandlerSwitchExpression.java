package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

class HandlerSwitchExpression extends Handler {

	private final StatementsFactory statementsFactory;
	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;
	private final UtilStatementConverter utilStatementConverter;

	@Inject
	HandlerSwitchExpression(UtilStatementConverter utilStatementConverter, UtilLayout utilLayout,
			ToExpressionConverter toExpressionConverter, StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	@Override
	org.emftext.language.java.expressions.Expression handle(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		org.emftext.language.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(toExpressionConverter.convert(switchExpr.getExpression()));
		utilStatementConverter.convertToSwitchCasesAndSet(result, switchExpr.statements());
		utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
