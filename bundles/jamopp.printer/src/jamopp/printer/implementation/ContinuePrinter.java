package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Continue;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ContinuePrinterInt;

class ContinuePrinter implements ContinuePrinterInt{

	@Override
	public void print(Continue element, BufferedWriter writer) throws IOException {
		writer.append("continue");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
