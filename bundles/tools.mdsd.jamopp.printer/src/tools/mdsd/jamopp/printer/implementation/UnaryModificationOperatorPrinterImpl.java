package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.PlusPlus;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryModificationOperatorPrinterImpl implements Printer<UnaryModificationOperator> {

	@Override
	public void print(final UnaryModificationOperator element, final BufferedWriter writer) throws IOException {
		if (element instanceof PlusPlus) {
			writer.append("++");
		} else {
			writer.append("--");
		}
	}

}
