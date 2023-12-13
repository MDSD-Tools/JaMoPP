package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.DefaultSwitchCasePrinterInt;

class DefaultSwitchCasePrinter implements DefaultSwitchCasePrinterInt {

	private final StatementPrinter StatementPrinter;

	@Inject
	public DefaultSwitchCasePrinter(jamopp.printer.implementation.StatementPrinter statementPrinter) {
		super();
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
