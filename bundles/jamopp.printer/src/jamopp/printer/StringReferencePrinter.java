package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.StringReference;

class StringReferencePrinter {

	static void print(StringReference element, BufferedWriter writer) throws IOException {
		writer.append("\"" + element.getValue() + "\"");
	}

}
