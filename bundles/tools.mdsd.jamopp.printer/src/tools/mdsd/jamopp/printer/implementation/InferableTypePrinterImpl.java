package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;

public class InferableTypePrinterImpl implements EmptyPrinter {

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append("var");
	}

}