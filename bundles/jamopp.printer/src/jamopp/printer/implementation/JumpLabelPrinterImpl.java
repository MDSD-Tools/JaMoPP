package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class JumpLabelPrinterImpl implements Printer<JumpLabel> {

	private final Printer<Statement> StatementPrinter;

	@Inject
	public JumpLabelPrinterImpl(Printer<Statement> statementPrinter) {
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
