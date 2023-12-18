package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.operators.Equal;
import tools.mdsd.jamopp.model.java.operators.EqualityOperator;
import tools.mdsd.jamopp.model.java.operators.NotEqual;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EqualityOperatorPrinterImpl implements Printer<EqualityOperator> {

	@Override
	public void print(EqualityOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Equal) {
			writer.append(" == ");
		} else if (element instanceof NotEqual) {
			writer.append(" != ");
		}
	}

}
