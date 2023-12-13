package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Equal;
import org.emftext.language.java.operators.EqualityOperator;
import org.emftext.language.java.operators.NotEqual;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.EqualityOperatorPrinterInt;

public class EqualityOperatorPrinter implements EqualityOperatorPrinterInt{

	@Override
	public void print(EqualityOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Equal) {
			writer.append(" == ");
		} else if (element instanceof NotEqual) {
			writer.append(" != ");
		}
	}

}
