package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class AssertStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public AssertStatementHandler(final StatementsFactory statementsFactory,
			final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final AssertStatement assertSt = (AssertStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.Assert result = statementsFactory.createAssert();
		result.setCondition(expressionConverterUtility.convert(assertSt.getExpression()));
		if (assertSt.getMessage() != null) {
			result.setErrorMessage(expressionConverterUtility.convert(assertSt.getMessage()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, assertSt);
		return result;
	}
}
