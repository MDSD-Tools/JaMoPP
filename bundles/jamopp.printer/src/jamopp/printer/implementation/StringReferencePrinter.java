package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.StringReference;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.StringReferencePrinterInt;

class StringReferencePrinter implements StringReferencePrinterInt{

	@Override
	public void print(StringReference element, BufferedWriter writer) throws IOException {
		writer.append("\"" + element.getValue() + "\"");
	}

}
