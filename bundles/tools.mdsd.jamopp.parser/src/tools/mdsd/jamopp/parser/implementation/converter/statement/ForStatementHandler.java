package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import tools.mdsd.jamopp.model.java.expressions.ExpressionsFactory;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ForStatementHandler implements StatementHandler {

	private final ExpressionsFactory expressionsFactory;
	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public ForStatementHandler(
			final Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter,
			final StatementsFactory statementsFactory,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			final UtilLayout layoutInformationConverter, final ExpressionsFactory expressionsFactory,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.expressionsFactory = expressionsFactory;
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toLocalVariableConverter = toLocalVariableConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final ForStatement forSt = (ForStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.ForLoop result = statementsFactory.createForLoop();
		if (forSt.initializers().size() == 1 && forSt.initializers().get(0) instanceof VariableDeclarationExpression) {
			result.setInit(
					toLocalVariableConverter.convert((VariableDeclarationExpression) forSt.initializers().get(0)));
		} else {
			final tools.mdsd.jamopp.model.java.expressions.ExpressionList ini = expressionsFactory
					.createExpressionList();
			forSt.initializers()
					.forEach(obj -> ini.getExpressions().add(expressionConverterUtility.convert((Expression) obj)));
			result.setInit(ini);
		}
		if (forSt.getExpression() != null) {
			result.setCondition(expressionConverterUtility.convert(forSt.getExpression()));
		}
		forSt.updaters().forEach(obj -> result.getUpdates().add(expressionConverterUtility.convert((Expression) obj)));
		result.setStatement(statementToStatementConverter.convert(forSt.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, forSt);
		return result;
	}
}
