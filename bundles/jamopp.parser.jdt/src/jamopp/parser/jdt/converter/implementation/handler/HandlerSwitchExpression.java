package jamopp.parser.jdt.converter.implementation.handler;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.handler.ExpressionHandler;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToSwitchCasesAndSetConverter;

public class HandlerSwitchExpression implements ExpressionHandler {

	private final StatementsFactory statementsFactory;
	private final IUtilLayout utilLayout;
	private final IUtilToSwitchCasesAndSetConverter utilStatementConverter;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter;

	@Inject
	HandlerSwitchExpression(IUtilToSwitchCasesAndSetConverter utilStatementConverter, IUtilLayout utilLayout,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverter,
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
