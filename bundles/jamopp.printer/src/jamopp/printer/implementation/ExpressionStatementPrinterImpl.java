package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.ExpressionStatement;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionStatementPrinterInt;

public class ExpressionStatementPrinterImpl implements ExpressionStatementPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public ExpressionStatementPrinterImpl(ExpressionPrinterInt expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ExpressionStatement element, BufferedWriter writer) throws IOException {
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(";\n");
	}



}
