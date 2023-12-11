package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

import jamopp.printer.interfaces.Printer;

class DefaultSwitchCasePrinter implements Printer<DefaultSwitchCase>{

	private final StatementPrinter StatementPrinter;
	
	public void print(DefaultSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
		writer.append("\n");
	}

}
