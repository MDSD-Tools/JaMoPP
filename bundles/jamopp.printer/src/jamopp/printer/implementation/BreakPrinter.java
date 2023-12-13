package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Break;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.BreakPrinterInt;

class BreakPrinter implements Printer<Break>, BreakPrinterInt{

	@Override
	public void print(Break element, BufferedWriter writer) throws IOException {
		writer.append("break");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
