package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ExpressionStatementPrinter implements Printer<ExpressionStatement> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ExpressionStatementPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	public void print(ExpressionStatement element, BufferedWriter writer) throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}

}
