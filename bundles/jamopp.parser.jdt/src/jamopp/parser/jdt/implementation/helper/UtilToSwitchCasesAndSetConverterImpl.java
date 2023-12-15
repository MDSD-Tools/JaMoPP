package jamopp.parser.jdt.implementation.helper;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.YieldStatement;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilToSwitchCasesAndSetConverter;

public class UtilToSwitchCasesAndSetConverterImpl implements UtilToSwitchCasesAndSetConverter {

	private final StatementsFactory statementsFactory;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> toExpressionConverterUtility;
	private final Converter<Statement, org.emftext.language.java.statements.Statement> toStatementConverter;
	private final Converter<SwitchCase, org.emftext.language.java.statements.SwitchCase> toSwitchCaseConverter;

	@Inject
	UtilToSwitchCasesAndSetConverterImpl(
			Converter<SwitchCase, org.emftext.language.java.statements.SwitchCase> toSwitchCaseConverter,
			StatementsFactory statementsFactory,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter) {
		this.statementsFactory = statementsFactory;
		this.toExpressionConverterUtility = expressionConverterUtility;
		this.toSwitchCaseConverter = toSwitchCaseConverter;
		this.toStatementConverter = statementToStatementConverter;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void convert(org.emftext.language.java.statements.Switch switchExprSt, List switchStatementList) {
		org.emftext.language.java.statements.SwitchCase currentCase = null;
		for (Object element : switchStatementList) {
			var st = (Statement) element;
			if (st.getNodeType() == ASTNode.SWITCH_CASE) {
				currentCase = this.toSwitchCaseConverter.convert((SwitchCase) st);
				switchExprSt.getCases().add(currentCase);
			} else if (currentCase instanceof org.emftext.language.java.statements.SwitchRule
					&& st.getNodeType() == ASTNode.YIELD_STATEMENT) {
				var ys = (YieldStatement) st;
				var exprSt = this.statementsFactory.createExpressionStatement();
				exprSt.setExpression(this.toExpressionConverterUtility.convert(ys.getExpression()));
				currentCase.getStatements().add(exprSt);
			} else {
				currentCase.getStatements().add(this.toStatementConverter.convert(st));
			}
		}
	}

}
