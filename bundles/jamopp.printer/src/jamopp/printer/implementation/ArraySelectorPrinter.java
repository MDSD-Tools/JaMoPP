package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.arrays.ArraySelector;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ArraySelectorPrinter implements Printer<ArraySelector> {

	private final AnnotablePrinter AnnotablePrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ArraySelectorPrinter(jamopp.printer.implementation.AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		AnnotablePrinter = annotablePrinter;
		ExpressionPrinter = expressionPrinter;
	}

	public void print(ArraySelector element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append("[");
		ExpressionPrinter.print(element.getPosition(), writer);
		writer.append("]");
	}

}
