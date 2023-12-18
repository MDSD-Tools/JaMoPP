package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.statements.Break;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class BreakPrinterImpl implements Printer<Break> {

	@Override
	public void print(Break element, BufferedWriter writer) throws IOException {
		writer.append("break");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
