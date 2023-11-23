package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;

public class JumpLabelPrinter {

	static void printJumpLabel(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.printStatement(element.getStatement(), writer);
	}

}
