package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.Addition;
import org.emftext.language.java.operators.AdditiveOperator;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AdditiveOperatorPrinterImpl implements Printer<AdditiveOperator> {

	@Override
	public void print(AdditiveOperator element, BufferedWriter writer) throws IOException {
		if (element instanceof Addition) {
			writer.append(" + ");
		} else {
			writer.append(" - ");
		}
	}

}
