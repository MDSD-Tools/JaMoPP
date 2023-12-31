package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class IfStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public IfStatementHandler(StatementsFactory statementsFactory,
			Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			UtilLayout layoutInformationConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		IfStatement ifSt = (IfStatement) statement;
		tools.mdsd.jamopp.model.java.statements.Condition result = statementsFactory.createCondition();
		result.setCondition(expressionConverterUtility.convert(ifSt.getExpression()));
		result.setStatement(statementToStatementConverter.convert(ifSt.getThenStatement()));
		if (ifSt.getElseStatement() != null) {
			result.setElseStatement(statementToStatementConverter.convert(ifSt.getElseStatement()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, ifSt);
		return result;
	}
}
