package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchCase;
import org.emftext.language.java.statements.Statement;

class DefaultSwitchCasePrinter {

	static void print(DefaultSwitchCase element, BufferedWriter writer) throws IOException {
		writer.append("default: ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
		writer.append("\n");
	}

}
