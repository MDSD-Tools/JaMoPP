package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.DefaultSwitchRule;
import org.emftext.language.java.statements.Statement;

public class DefaultSwitchRulePrinter {

	static void printDefaultSwitchRule(DefaultSwitchRule element, BufferedWriter writer) throws IOException {
		writer.append("default -> ");
		for (Statement s : element.getStatements()) {
			StatementPrinter.printStatement(s, writer);
		}
	}

}
