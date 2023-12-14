package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.InferableTypePrinterInt;

public class InferableTypePrinterImpl implements InferableTypePrinterInt {

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append("var");
	}

}
