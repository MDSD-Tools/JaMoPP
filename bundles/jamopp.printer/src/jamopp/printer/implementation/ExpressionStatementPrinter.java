package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExpressionStatementPrinterInt;

class ExpressionStatementPrinter implements ExpressionStatementPrinterInt {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ExpressionStatementPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ExpressionStatement element, BufferedWriter writer) throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}

}
