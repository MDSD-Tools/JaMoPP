package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ExpressionStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter;

	@Inject
	public ExpressionStatementHandler(
			final Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> toLocalVariableConverter,
			final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toLocalVariableConverter = toLocalVariableConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		tools.mdsd.jamopp.model.java.statements.Statement statementResult;
		final ExpressionStatement exprSt = (ExpressionStatement) statement;
		if (exprSt.getExpression().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION) {
			final tools.mdsd.jamopp.model.java.statements.LocalVariableStatement result = statementsFactory
					.createLocalVariableStatement();
			result.setVariable(
					toLocalVariableConverter.convert((VariableDeclarationExpression) exprSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
			statementResult = result;
		} else {
			final tools.mdsd.jamopp.model.java.statements.ExpressionStatement result = statementsFactory
					.createExpressionStatement();
			result.setExpression(expressionConverterUtility.convert(exprSt.getExpression()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, exprSt);
			statementResult = result;
		}
		return statementResult;
	}
}
