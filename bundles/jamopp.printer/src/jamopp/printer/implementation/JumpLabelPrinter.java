package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class JumpLabelPrinter implements Printer<JumpLabel> {

	private final StatementPrinter StatementPrinter;

	@Inject
	public JumpLabelPrinter(jamopp.printer.implementation.StatementPrinter statementPrinter) {
		super();
		StatementPrinter = statementPrinter;
	}

	public void print(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
