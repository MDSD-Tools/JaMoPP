package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArraySelector;

import jamopp.printer.interfaces.Printer;

class ArraySelectorPrinter implements Printer<ArraySelector>{

	private final AnnotablePrinter AnnotablePrinter;
	private final ExpressionPrinter ExpressionPrinter;
	
	public void print(ArraySelector element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append("[");
		ExpressionPrinter.print(element.getPosition(), writer);
		writer.append("]");
	}

}
