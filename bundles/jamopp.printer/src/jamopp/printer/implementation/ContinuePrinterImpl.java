package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Continue;

import jamopp.printer.interfaces.Printer;

public class ContinuePrinterImpl implements Printer<Continue> {

	@Override
	public void print(Continue element, BufferedWriter writer) throws IOException {
		writer.append("continue");
		if (element.getTarget() != null) {
			writer.append(" " + element.getTarget().getName());
		}
		writer.append(";\n");
	}

}
