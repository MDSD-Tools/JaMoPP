package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.EmptyPrinter;
import jamopp.printer.interfaces.printer.InferableTypePrinterInt;

public class InferableTypePrinter implements  InferableTypePrinterInt{

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append("var");
	}

}
