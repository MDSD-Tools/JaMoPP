package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class DefaultSwitchCasePrinterImpl implements Printer<DefaultSwitchCase> {

	private final Printer<Statement> StatementPrinter;

	@Inject
	public DefaultSwitchCasePrinterImpl(Printer<Statement> statementPrinter) {
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
