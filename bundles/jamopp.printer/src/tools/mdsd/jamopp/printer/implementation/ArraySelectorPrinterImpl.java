package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.arrays.ArraySelector;
import tools.mdsd.jamopp.model.java.expressions.Expression;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ArraySelectorPrinterImpl implements Printer<ArraySelector> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public ArraySelectorPrinterImpl(Printer<Annotable> annotablePrinter, Printer<Expression> expressionPrinter) {
		this.annotablePrinter = annotablePrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ArraySelector element, BufferedWriter writer) throws IOException {
		this.annotablePrinter.print(element, writer);
		writer.append("[");
		this.expressionPrinter.print(element.getPosition(), writer);
		writer.append("]");
	}

}
