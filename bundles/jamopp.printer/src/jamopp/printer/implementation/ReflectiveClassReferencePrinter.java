package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.EmptyPrinter;
import jamopp.printer.interfaces.printer.ReflectiveClassReferencePrinterInt;

public class ReflectiveClassReferencePrinter implements  ReflectiveClassReferencePrinterInt {

	@Override
	public  void print(BufferedWriter writer) throws IOException {
		writer.append("class");
	}

}
