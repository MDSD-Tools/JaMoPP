package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.YieldStatement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class YieldStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public YieldStatementHandler(final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final YieldStatement yieldSt = (YieldStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.YieldStatement result = statementsFactory.createYieldStatement();
		if (yieldSt.getExpression() != null) {
			result.setYieldExpression(expressionConverterUtility.convert(yieldSt.getExpression()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, yieldSt);
		return result;
	}

}
