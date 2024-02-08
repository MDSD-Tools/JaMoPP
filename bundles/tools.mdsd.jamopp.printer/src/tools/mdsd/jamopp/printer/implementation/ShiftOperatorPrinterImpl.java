package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.LeftShift;
import tools.mdsd.jamopp.model.java.operators.RightShift;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ShiftOperatorPrinterImpl implements Printer<ShiftOperator> {

	@Override
	public void print(final ShiftOperator element, final BufferedWriter writer) throws IOException {
		if (element instanceof LeftShift) {
			writer.append(" << ");
		} else if (element instanceof RightShift) {
			writer.append(" >> ");
		} else {
			writer.append(" >>> ");
		}
	}

}
