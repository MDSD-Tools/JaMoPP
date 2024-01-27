package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ThrowStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public ThrowStatementHandler(final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final ThrowStatement throwSt = (ThrowStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.Throw result = statementsFactory.createThrow();
		result.setThrowable(expressionConverterUtility.convert(throwSt.getExpression()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, throwSt);
		return result;
	}

}
