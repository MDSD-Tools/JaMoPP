package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Assert;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AssertPrinterInt;

class AssertPrinter implements Printer<Assert>, AssertPrinterInt {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AssertPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(Assert element, BufferedWriter writer) throws IOException {
		writer.append("assert ");
		ExpressionPrinter.print(element.getCondition(), writer);
		if (element.getErrorMessage() != null) {
			writer.append(" : ");
			ExpressionPrinter.print(element.getErrorMessage(), writer);
		}
		writer.append(";\n");
	}

}
