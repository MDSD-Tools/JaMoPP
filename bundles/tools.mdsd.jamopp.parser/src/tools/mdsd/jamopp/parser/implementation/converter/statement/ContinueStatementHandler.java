package tools.mdsd.jamopp.parser.implementation.converter.statement;

import java.util.Set;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Statement;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;

public class ContinueStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final Set<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels;

	@Inject
	public ContinueStatementHandler(final StatementsFactory statementsFactory,
			final UtilLayout layoutInformationConverter,
			final Set<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.currentJumpLabels = currentJumpLabels;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final ContinueStatement conSt = (ContinueStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.Continue result = statementsFactory.createContinue();
		if (conSt.getLabel() != null) {
			final tools.mdsd.jamopp.model.java.statements.JumpLabel proxyTarget = currentJumpLabels.stream()
					.filter(label -> label.getName().equals(conSt.getLabel().getIdentifier())).findFirst().get();
			result.setTarget(proxyTarget);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, conSt);
		return result;
	}
}
