package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.Division;
import tools.mdsd.jamopp.model.java.operators.Multiplication;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;

import tools.mdsd.jamopp.printer.interfaces.Printer;

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
