package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.TextBlockReference;

import jamopp.printer.interfaces.Printer;

public class TextBlockReferencePrinterImpl implements Printer<TextBlockReference> {

	@Override
	public void print(TextBlockReference element, BufferedWriter writer) throws IOException {
		writer.append("\"\"\"\n");
		writer.append(element.getValue());
		writer.append("\n\"\"\"");
	}

}
