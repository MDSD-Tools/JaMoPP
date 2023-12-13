package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.LeftShift;
import org.emftext.language.java.operators.RightShift;
import org.emftext.language.java.operators.ShiftOperator;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ShiftOperatorPrinterInt;

class ShiftOperatorPrinter implements ShiftOperatorPrinterInt{

	@Override
	public void print(ShiftOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof LeftShift) {
			writer.append(" << ");
		} else if (element instanceof RightShift) {
			writer.append(" >> ");
		} else {
			writer.append(" >>> ");
		}
	}

}
