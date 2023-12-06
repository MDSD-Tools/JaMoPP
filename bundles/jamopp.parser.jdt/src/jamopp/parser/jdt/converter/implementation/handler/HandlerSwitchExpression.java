package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilToSwitchCasesAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;

public class HandlerSwitchExpression implements ExpressionHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout utilLayout;
	private final UtilToSwitchCasesAndSetConverter utilStatementConverter;
	private final ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerSwitchExpression(UtilToSwitchCasesAndSetConverter utilStatementConverter, UtilLayout utilLayout,
			ToConverter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverter = toExpressionConverter;
		this.utilLayout = utilLayout;
		this.utilStatementConverter = utilStatementConverter;
	}

	@Override
	public org.emftext.language.java.expressions.Expression handle(Expression expr) {
		SwitchExpression switchExpr = (SwitchExpression) expr;
		org.emftext.language.java.statements.Switch result = statementsFactory.createSwitch();
		result.setVariable(toExpressionConverter.convert(switchExpr.getExpression()));
		utilStatementConverter.convert(result, switchExpr.statements());
		utilLayout.convertToMinimalLayoutInformation(result, switchExpr);
		return result;
	}

}
