package jamopp.parser.jdt.converter.implementation;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.YieldStatement;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

public class UtilToSwitchCasesAndSetConverterImpl implements UtilToSwitchCasesAndSetConverter{

	private final StatementsFactory statementsFactory;
	private final ToExpressionConverter expressionConverterUtility;
	private final ToSwitchCaseConverter toSwitchCaseConverter;
	private final StatementToStatementConverter statementToStatementConverter;

	@Inject
	UtilToSwitchCasesAndSetConverterImpl(ToSwitchCaseConverter toSwitchCaseConverter, StatementsFactory statementsFactory,
			ToExpressionConverter expressionConverterUtility,
			StatementToStatementConverter statementToStatementConverter) {
		this.statementsFactory = statementsFactory;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCaseConverter = toSwitchCaseConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@SuppressWarnings("rawtypes")
	public void convert(org.emftext.language.java.statements.Switch switchExprSt,
			List switchStatementList) {
		org.emftext.language.java.statements.SwitchCase currentCase = null;
		for (Object element : switchStatementList) {
			Statement st = (Statement) element;
			if (st.getNodeType() == ASTNode.SWITCH_CASE) {
				currentCase = toSwitchCaseConverter.convertToSwitchCase((SwitchCase) st);
				switchExprSt.getCases().add(currentCase);
			} else if (currentCase instanceof org.emftext.language.java.statements.SwitchRule
					&& st.getNodeType() == ASTNode.YIELD_STATEMENT) {
				YieldStatement ys = (YieldStatement) st;
				org.emftext.language.java.statements.ExpressionStatement exprSt = statementsFactory
						.createExpressionStatement();
				exprSt.setExpression(expressionConverterUtility.convert(ys.getExpression()));
				currentCase.getStatements().add(exprSt);
			} else {
				currentCase.getStatements().add(statementToStatementConverter.convert(st));
			}
		}
	}
	
}
