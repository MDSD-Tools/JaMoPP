package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class LabeledStatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final HashSet<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	@Inject
	public LabeledStatementHandler(UtilNamedElement utilNamedElement, StatementsFactory statementsFactory,
			Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			UtilLayout layoutInformationConverter,
			HashSet<tools.mdsd.jamopp.model.java.statements.JumpLabel> currentJumpLabels) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.currentJumpLabels = currentJumpLabels;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		LabeledStatement labelSt = (LabeledStatement) statement;
		tools.mdsd.jamopp.model.java.statements.JumpLabel result = statementsFactory.createJumpLabel();
		utilNamedElement.setNameOfElement(labelSt.getLabel(), result);
		currentJumpLabels.add(result);
		result.setStatement(statementToStatementConverter.convert(labelSt.getBody()));
		currentJumpLabels.remove(result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, labelSt);
		return result;
	}

}
