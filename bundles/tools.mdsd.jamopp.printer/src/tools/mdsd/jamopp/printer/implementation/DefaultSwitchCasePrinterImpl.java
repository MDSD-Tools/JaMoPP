package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.statements.DefaultSwitchCase;
import tools.mdsd.jamopp.model.java.statements.Statement;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class DefaultSwitchCasePrinterImpl implements Printer<DefaultSwitchCase> {

	private final Printer<Statement> statementPrinter;

	@Inject
	public DefaultSwitchCasePrinterImpl(Printer<Statement> statementPrinter) {
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(DefaultSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (Statement s : element.getStatements()) {
			this.statementPrinter.print(s, writer);
		}
		writer.append("\n");
	}

}
