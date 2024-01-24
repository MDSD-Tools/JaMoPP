package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.statements.JumpLabel;
import tools.mdsd.jamopp.model.java.statements.Statement;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class JumpLabelPrinterImpl implements Printer<JumpLabel> {

	private final Printer<Statement> statementPrinter;

	@Inject
	public JumpLabelPrinterImpl(final Printer<Statement> statementPrinter) {
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(final JumpLabel element, final BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		statementPrinter.print(element.getStatement(), writer);
	}

}
