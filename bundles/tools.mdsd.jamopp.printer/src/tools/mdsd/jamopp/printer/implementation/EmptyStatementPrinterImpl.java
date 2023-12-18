package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.printer.interfaces.EmptyPrinter;

public class EmptyStatementPrinterImpl implements EmptyPrinter {

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append(";\n");
	}

}
