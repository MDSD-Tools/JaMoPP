package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Division;
import org.emftext.language.java.operators.Multiplication;
import org.emftext.language.java.operators.MultiplicativeOperator;

import jamopp.printer.interfaces.Printer;

public class MultiplicativeOperatorPrinterImpl implements Printer<MultiplicativeOperator> {

	@Override
	public void print(MultiplicativeOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Multiplication) {
			writer.append(" * ");
		} else if (element instanceof Division) {
			writer.append(" / ");
		} else {
			writer.append(" % ");
		}
	}

}
