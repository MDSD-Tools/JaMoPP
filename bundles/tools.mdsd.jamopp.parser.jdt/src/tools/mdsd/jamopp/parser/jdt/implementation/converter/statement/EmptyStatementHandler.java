package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class EmptyStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;

	@Inject
	public EmptyStatementHandler(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		tools.mdsd.jamopp.model.java.statements.EmptyStatement result = statementsFactory.createEmptyStatement();
		layoutInformationConverter.convertToMinimalLayoutInformation(result, statement);
		return result;
	}
}
