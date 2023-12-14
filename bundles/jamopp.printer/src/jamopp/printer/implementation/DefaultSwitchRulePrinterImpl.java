package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class DefaultSwitchRulePrinterImpl implements Printer<DefaultSwitchRule> {

	private final Printer<Statement> StatementPrinter;

	@Inject
	public DefaultSwitchRulePrinterImpl(Printer<Statement> statementPrinter) {
		StatementPrinter = statementPrinter;
	}

	@Override
	public void print(DefaultSwitchRule element, BufferedWriter writer) throws IOException {
		writer.append("default -> ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
	}



}
