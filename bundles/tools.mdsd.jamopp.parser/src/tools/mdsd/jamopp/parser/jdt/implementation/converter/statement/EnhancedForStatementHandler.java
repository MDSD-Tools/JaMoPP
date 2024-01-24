package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class EnhancedForStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public EnhancedForStatementHandler(
			final Converter<SingleVariableDeclaration, OrdinaryParameter> toOrdinaryParameterConverter,
			final StatementsFactory statementsFactory,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toOrdinaryParameterConverter = toOrdinaryParameterConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final EnhancedForStatement forSt = (EnhancedForStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.ForEachLoop result = statementsFactory.createForEachLoop();
		result.setNext(toOrdinaryParameterConverter.convert(forSt.getParameter()));
		result.setCollection(expressionConverterUtility.convert(forSt.getExpression()));
		result.setStatement(statementToStatementConverter.convert(forSt.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
		return result;
	}
}
