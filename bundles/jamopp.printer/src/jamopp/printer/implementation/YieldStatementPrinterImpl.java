package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.YieldStatementPrinterInt;

public class YieldStatementPrinterImpl implements YieldStatementPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public YieldStatementPrinterImpl(ExpressionPrinterInt expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
