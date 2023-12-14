package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
