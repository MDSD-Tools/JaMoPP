package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.DefaultSwitchRule;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class DefaultSwitchRulePrinterImpl implements Printer<DefaultSwitchRule> {

	private final Printer<Statement> statementPrinter;

	@Inject
	public DefaultSwitchRulePrinterImpl(final Printer<Statement> statementPrinter) {
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final DefaultSwitchRule element, final BufferedWriter writer) throws IOException {
		writer.append("default -> ");
		for (final Statement s : element.getStatements()) {
			statementPrinter.print(s, writer);
		}
	}

}
