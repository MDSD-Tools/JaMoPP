package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.references.StringReference;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class StringReferencePrinterImpl implements Printer<StringReference> {

	@Override
	public void print(StringReference element, BufferedWriter writer) throws IOException {
		writer.append("\"" + element.getValue() + "\"");
	}

}
