package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class DoStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public DoStatementHandler(final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		statementToStatementConverter = statementConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final DoStatement doSt = (DoStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.DoWhileLoop result = statementsFactory.createDoWhileLoop();
		result.setCondition(expressionConverterUtility.convert(doSt.getExpression()));
		result.setStatement(statementToStatementConverter.convert(doSt.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, doSt);
		return result;
	}
}
