package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArraySelector;
import org.emftext.language.java.expressions.Expression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ArraySelectorPrinterImpl implements Printer<ArraySelector> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public ArraySelectorPrinterImpl(Printer<Annotable> annotablePrinter, Printer<Expression> expressionPrinter) {
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
