package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.operators.PlusPlus;
import org.emftext.language.java.operators.UnaryModificationOperator;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.UnaryModificationOperatorPrinterInt;

class UnaryModificationOperatorPrinter implements UnaryModificationOperatorPrinterInt{

	@Override
	public void print(UnaryModificationOperator element, BufferedWriter writer)
			throws IOException {
		if (element instanceof PlusPlus) {
			writer.append("++");
		} else {
			writer.append("--");
		}
	}

}
