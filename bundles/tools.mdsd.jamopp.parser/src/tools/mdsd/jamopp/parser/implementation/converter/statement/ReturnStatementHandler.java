package tools.mdsd.jamopp.parser.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ReturnStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public ReturnStatementHandler(final StatementsFactory statementsFactory,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final ReturnStatement retSt = (ReturnStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.Return result = statementsFactory.createReturn();
		if (retSt.getExpression() != null) {
			result.setReturnValue(expressionConverterUtility.convert(retSt.getExpression()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, retSt);
		return result;
	}
}
