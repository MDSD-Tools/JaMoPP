package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.StringReference;

public class StringReferencePrinter {

	static void printStringReference(StringReference element, BufferedWriter writer) throws IOException {
		writer.append("\"" + element.getValue() + "\"");
	}

}
