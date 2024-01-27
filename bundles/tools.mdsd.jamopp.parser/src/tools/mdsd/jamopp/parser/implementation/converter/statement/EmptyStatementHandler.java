package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class EmptyStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;

	@Inject
	public EmptyStatementHandler(final StatementsFactory statementsFactory,
			final UtilLayout layoutInformationConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final tools.mdsd.jamopp.model.java.statements.EmptyStatement result = statementsFactory.createEmptyStatement();
		layoutInformationConverter.convertToMinimalLayoutInformation(result, statement);
		return result;
	}
}
