package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.StringReference;

import jamopp.printer.interfaces.Printer;

public class StringReferencePrinterImpl implements Printer<StringReference> {

	@Override
	public void print(StringReference element, BufferedWriter writer) throws IOException {
		writer.append("\"" + element.getValue() + "\"");
	}

}
