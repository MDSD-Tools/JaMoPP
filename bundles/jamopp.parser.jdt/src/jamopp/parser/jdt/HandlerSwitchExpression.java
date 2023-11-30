package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;

import com.google.inject.Inject;

class HandlerSwitchExpression {

	private final ToExpressionConverter toExpressionConverter;
	private final UtilLayout utilLayout;
	private final UtilStatementConverter utilStatementConverter;

	@Inject
	HandlerSwitchExpression(UtilStatementConverter utilStatementConverter, UtilLayout utilLayout,
			ToExpressionConverter toExpressionConverter) {
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	org.emftext.language.java.expressions.Expression handleSwitchExpression(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		org.emftext.language.java.statements.Switch result = org.emftext.language.java.statements.StatementsFactory.eINSTANCE
				.createSwitch();
		result.setVariable(toExpressionConverter.convertToExpression(switchExpr.getExpression()));
		utilStatementConverter.convertToSwitchCasesAndSet(result, switchExpr.statements());
		utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
