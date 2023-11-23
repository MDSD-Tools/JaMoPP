package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Break;

class BreakPrinter {

	static void print(Break element, BufferedWriter writer) throws IOException {
		writer.append("break");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
