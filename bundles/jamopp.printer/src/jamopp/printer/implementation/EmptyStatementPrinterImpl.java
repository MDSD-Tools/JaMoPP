package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.EmptyStatementPrinterInt;

public class EmptyStatementPrinterImpl implements EmptyStatementPrinterInt {

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append(";\n");
	}

}
