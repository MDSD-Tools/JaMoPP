package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;

public class SwitchStatementHandler implements StatementHandler {

	private final Converter<SwitchStatement, Switch> switchToSwitchConverter;

	@Inject
	public SwitchStatementHandler(Converter<SwitchStatement, Switch> switchToSwitchConverter) {
		this.switchToSwitchConverter = switchToSwitchConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		return switchToSwitchConverter.convert((SwitchStatement) statement);
	}
}
