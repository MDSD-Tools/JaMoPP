package tools.mdsd.jamopp.parser.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class IfStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public IfStatementHandler(final StatementsFactory statementsFactory,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final IfStatement ifSt = (IfStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.Condition result = statementsFactory.createCondition();
		result.setCondition(expressionConverterUtility.convert(ifSt.getExpression()));
		result.setStatement(statementToStatementConverter.convert(ifSt.getThenStatement()));
		if (ifSt.getElseStatement() != null) {
			result.setElseStatement(statementToStatementConverter.convert(ifSt.getElseStatement()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ifSt);
		return result;
	}
}
