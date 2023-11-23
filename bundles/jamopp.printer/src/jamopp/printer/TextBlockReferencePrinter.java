package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.TextBlockReference;

public class TextBlockReferencePrinter {

	static void printTextBlockReference(TextBlockReference element, BufferedWriter writer) throws IOException {
		writer.append("\"\"\"\n");
		writer.append(element.getValue());
		writer.append("\n\"\"\"");
	}

}
