package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import jamopp.printer.interfaces.printer.EmptyMemberPrinterInt;

public class EmptyMemberPrinterImpl implements EmptyMemberPrinterInt {

	@Override
	public void print(BufferedWriter writer) throws IOException {
		writer.append(";\n\n");
	}

}
