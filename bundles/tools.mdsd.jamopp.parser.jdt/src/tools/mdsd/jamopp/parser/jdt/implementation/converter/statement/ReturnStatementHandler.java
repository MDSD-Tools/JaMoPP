package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ReturnStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public ReturnStatementHandler(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.expressionConverterUtility = expressionConverterUtility;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		ReturnStatement retSt = (ReturnStatement) statement;
		tools.mdsd.jamopp.model.java.statements.Return result = statementsFactory.createReturn();
		if (retSt.getExpression() != null) {
			result.setReturnValue(expressionConverterUtility.convert(retSt.getExpression()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, retSt);
		return result;
	}
}
