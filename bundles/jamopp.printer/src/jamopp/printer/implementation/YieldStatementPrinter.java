package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.statements.YieldStatement;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class YieldStatementPrinter implements Printer<YieldStatement> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public YieldStatementPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	public void print(YieldStatement element, BufferedWriter writer) throws IOException {
		writer.append("yield ");
		ExpressionPrinter.print(element.getYieldExpression(), writer);
		writer.append(";\n");
	}

}
