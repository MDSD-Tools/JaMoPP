package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Throw;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ThrowPrinter implements Printer<Throw> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ThrowPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	public void print(Throw element, BufferedWriter writer) throws IOException {
		writer.append("throw ");
		ExpressionPrinter.print(element.getThrowable(), writer);
		writer.append(";\n");
	}

}
