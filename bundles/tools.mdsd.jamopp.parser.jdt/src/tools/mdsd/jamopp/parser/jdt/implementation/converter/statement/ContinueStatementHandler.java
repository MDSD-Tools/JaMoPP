package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ContinueStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final HashSet<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels;

	@Inject
	public ContinueStatementHandler(StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			HashSet<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.currentJumpLabels = currentJumpLabels;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		ContinueStatement conSt = (ContinueStatement) statement;
		tools.mdsd.jamopp.model.java.statements.Continue result = statementsFactory.createContinue();
		if (conSt.getLabel() != null) {
			tools.mdsd.jamopp.model.java.statements.JumpLabel proxyTarget = currentJumpLabels.stream()
					.filter(label -> label.getName().equals(conSt.getLabel().getIdentifier())).findFirst().get();
			result.setTarget(proxyTarget);
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, conSt);
		return result;
	}
}
