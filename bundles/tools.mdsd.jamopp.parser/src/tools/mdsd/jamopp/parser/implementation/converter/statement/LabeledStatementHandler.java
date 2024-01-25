package tools.mdsd.jamopp.parser.implementation.converter.statement;

import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;

public class LabeledStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final Set<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public LabeledStatementHandler(final UtilNamedElement utilNamedElement, final StatementsFactory statementsFactory,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			final UtilLayout layoutInformationConverter,
			final Set<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.currentJumpLabels = currentJumpLabels;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final LabeledStatement labelSt = (LabeledStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.JumpLabel result = statementsFactory.createJumpLabel();
		utilNamedElement.setNameOfElement(labelSt.getLabel(), result);
		currentJumpLabels.add(result);
		result.setStatement(statementToStatementConverter.convert(labelSt.getBody()));
		currentJumpLabels.remove(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, labelSt);
		return result;
	}

}
