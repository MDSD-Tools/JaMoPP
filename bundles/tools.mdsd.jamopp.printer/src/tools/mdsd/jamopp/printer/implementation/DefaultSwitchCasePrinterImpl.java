package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.DefaultSwitchCase;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class DefaultSwitchCasePrinterImpl implements Printer<DefaultSwitchCase> {

	private final Printer<Statement> statementPrinter;

	@Inject
	public DefaultSwitchCasePrinterImpl(final Printer<Statement> statementPrinter) {
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final DefaultSwitchCase element, final BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (final Statement s : element.getStatements()) {
			statementPrinter.print(s, writer);
		}
		writer.append("\n");
	}

}
