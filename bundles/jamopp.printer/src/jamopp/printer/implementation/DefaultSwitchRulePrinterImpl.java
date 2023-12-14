package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.Statement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class DefaultSwitchRulePrinterImpl implements Printer<DefaultSwitchRule> {

	private final Printer<Statement> statementPrinter;

	@Inject
	public DefaultSwitchRulePrinterImpl(Printer<Statement> statementPrinter) {
		this.statementPrinter = statementPrinter;
	}

	@Override
	public void print(DefaultSwitchRule element, BufferedWriter writer) throws IOException {
		writer.append("default -> ");
		for (Statement s : element.getStatements()) {
			this.statementPrinter.print(s, writer);
		}
	}

}
