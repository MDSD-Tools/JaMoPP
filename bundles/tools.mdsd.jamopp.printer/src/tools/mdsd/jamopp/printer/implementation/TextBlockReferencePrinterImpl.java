package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.references.TextBlockReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TextBlockReferencePrinterImpl implements Printer<TextBlockReference> {

	@Override
	public void print(final TextBlockReference element, final BufferedWriter writer) throws IOException {
		writer.append("\"\"\"\n");
		writer.append(element.getValue());
		writer.append("\n\"\"\"");
	}

}
