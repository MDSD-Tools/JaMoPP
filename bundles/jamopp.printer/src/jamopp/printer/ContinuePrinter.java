package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Continue;

class ContinuePrinter {

	static void print(Continue element, BufferedWriter writer) throws IOException {
		writer.append("continue");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
