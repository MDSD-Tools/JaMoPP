package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

public class DefaultSwitchCasePrinter {

	static void printDefaultSwitchCase(DefaultSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.printStatement(s, writer);
		}
		writer.append("\n");
	}

}
