package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.DefaultSwitchCasePrinterInt;
import jamopp.printer.interfaces.printer.StatementPrinterInt;

public class DefaultSwitchCasePrinter implements DefaultSwitchCasePrinterInt {

	private final StatementPrinterInt StatementPrinter;

	@Inject
	public DefaultSwitchCasePrinter(StatementPrinterInt statementPrinter) {
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(DefaultSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
		writer.append("\n");
	}

}
