package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.Return;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ReturnPrinter implements Printer<Return> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ReturnPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	public void print(Return element, BufferedWriter writer) throws IOException {
		writer.append("return");
		if (element.getReturnValue() != null) {
			writer.append(" ");
			ExpressionPrinter.print(element.getReturnValue(), writer);
		}
		writer.append(";\n");
	}

}
