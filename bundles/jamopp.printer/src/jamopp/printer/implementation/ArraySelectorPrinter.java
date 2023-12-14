package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArraySelector;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ArraySelectorPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class ArraySelectorPrinter implements ArraySelectorPrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public ArraySelectorPrinter(AnnotablePrinterInt annotablePrinter, ExpressionPrinterInt expressionPrinter) {
		AnnotablePrinter = annotablePrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ArraySelector element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append("[");
		ExpressionPrinter.print(element.getPosition(), writer);
		writer.append("]");
	}

	

}
