package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Assert;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AssertPrinter implements Printer<Assert> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AssertPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

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
