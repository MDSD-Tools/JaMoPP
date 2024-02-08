package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.List;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.YieldStatement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToSwitchCasesAndSetConverter;

public class ToSwitchCasesAndSetConverterImpl implements ToSwitchCasesAndSetConverter {

	private final StatementsFactory statementsFactory;
	private final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase> toSwitchCaseConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public ToSwitchCasesAndSetConverterImpl(
			final Converter<SwitchCase, tools.mdsd.jamopp.model.java.statements.SwitchCase> toSwitchCaseConverter,
			final StatementsFactory statementsFactory,
			final Converter<org.eclipse.jdt.core.dom.Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter) {
		this.statementsFactory = statementsFactory;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toSwitchCaseConverter = toSwitchCaseConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void convert(final tools.mdsd.jamopp.model.java.statements.Switch switchExprSt,
			final List switchStatementList) {
		tools.mdsd.jamopp.model.java.statements.SwitchCase currentCase = null;
		for (final Object element : switchStatementList) {
			final Statement statement = (Statement) element;
			if (statement.getNodeType() == ASTNode.SWITCH_CASE) {
				currentCase = toSwitchCaseConverter.convert((SwitchCase) statement);
				switchExprSt.getCases().add(currentCase);
			} else if (currentCase instanceof tools.mdsd.jamopp.model.java.statements.SwitchRule
					&& statement.getNodeType() == ASTNode.YIELD_STATEMENT) {
				final YieldStatement yieldStatement = (YieldStatement) statement;
				final tools.mdsd.jamopp.model.java.statements.ExpressionStatement exprSt = statementsFactory
						.createExpressionStatement();
				exprSt.setExpression(expressionConverterUtility.convert(yieldStatement.getExpression()));
				currentCase.getStatements().add(exprSt);
			} else if (currentCase != null) {
				currentCase.getStatements().add(statementToStatementConverter.convert(statement));
			}
		}
	}

}
