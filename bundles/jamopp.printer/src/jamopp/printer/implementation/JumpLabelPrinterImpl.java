package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.JumpLabelPrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class JumpLabelPrinterImpl implements JumpLabelPrinterInt {

	private final StatementPrinterInt StatementPrinter;

	@Inject
	public JumpLabelPrinterImpl(StatementPrinterInt statementPrinter) {
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
