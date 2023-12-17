package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.YieldStatement;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;

public class UtilToSwitchCasesAndSetConverterImpl implements UtilToSwitchCasesAndSetConverter {

	private final StatementsFactory statementsFactory;
	private final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase> toSwitchCaseConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	UtilToSwitchCasesAndSetConverterImpl(
			Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase> toSwitchCaseConverter,
			StatementsFactory statementsFactory,
			Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter) {
		this.statementsFactory = statementsFactory;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCaseConverter = toSwitchCaseConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@SuppressWarnings("rawtypes")
	public void convert(tools.mdsd.jamopp.model.java.statements.Switch switchExprSt, List switchStatementList) {
		tools.mdsd.jamopp.model.java.statements.SwitchCase currentCase = null;
		for (Object element : switchStatementList) {
			Statement st = (Statement) element;
			if (st.getNodeType() == ASTNode.SWITCH_CASE) {
				currentCase = toSwitchCaseConverter.convert((SwitchCase) st);
				switchExprSt.getCases().add(currentCase);
			} else if (currentCase instanceof tools.mdsd.jamopp.model.java.statements.SwitchRule
					&& st.getNodeType() == ASTNode.YIELD_STATEMENT) {
				YieldStatement ys = (YieldStatement) st;
				tools.mdsd.jamopp.model.java.statements.ExpressionStatement exprSt = statementsFactory
						.createExpressionStatement();
				exprSt.setExpression(expressionConverterUtility.convert(ys.getExpression()));
				currentCase.getStatements().add(exprSt);
			} else {
				currentCase.getStatements().add(statementToStatementConverter.convert(st));
			}
		}
	}

}
