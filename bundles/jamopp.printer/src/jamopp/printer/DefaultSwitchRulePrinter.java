package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.Statement;

class DefaultSwitchRulePrinter {

	static void print(DefaultSwitchRule element, BufferedWriter writer) throws IOException {
		writer.append("default -> ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.print(s, writer);
		}
	}

}
