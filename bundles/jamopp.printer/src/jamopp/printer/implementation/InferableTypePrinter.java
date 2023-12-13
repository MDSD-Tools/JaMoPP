package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.EmptyPrinter;
import jamopp.printer.interfaces.printer.InferableTypePrinterInt;

class InferableTypePrinter implements EmptyPrinter, InferableTypePrinterInt{

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append("var");
	}

}
