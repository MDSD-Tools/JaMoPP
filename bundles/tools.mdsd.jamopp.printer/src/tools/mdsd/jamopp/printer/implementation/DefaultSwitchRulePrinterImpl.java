package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.statements.DefaultSwitchRule;
import tools.mdsd.jamopp.model.java.statements.Statement;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

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
