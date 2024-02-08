package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.Addition;
import tools.mdsd.jamopp.model.java.operators.Negate;
import tools.mdsd.jamopp.model.java.operators.Subtraction;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryOperatorPrinterImpl implements Printer<UnaryOperator> {

	@Override
	public void print(final UnaryOperator element, final BufferedWriter writer) throws IOException {
		if (element instanceof Addition) {
			writer.append("+");
		} else if (element instanceof Subtraction) {
			writer.append("-");
		} else if (element instanceof Negate) {
			writer.append("!");
		} else {
			writer.append("~");
		}
	}

}
