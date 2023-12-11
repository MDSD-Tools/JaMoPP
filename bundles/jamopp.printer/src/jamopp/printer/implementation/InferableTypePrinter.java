package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.EmptyPrinter;

class InferableTypePrinter implements EmptyPrinter{

	public void print(BufferedWriter writer) throws IOException {
		writer.append("var");
	}

}
