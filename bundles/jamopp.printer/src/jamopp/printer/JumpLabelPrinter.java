package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.JumpLabel;

class JumpLabelPrinter {

	static void print(JumpLabel element, BufferedWriter writer) throws IOException {
		writer.append(element.getName() + ": ");
		StatementPrinter.print(element.getStatement(), writer);
	}

}
