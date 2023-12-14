package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.NestedExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.NestedExpressionPrinterInt;

public class NestedExpressionPrinterImpl implements NestedExpressionPrinterInt {

	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public NestedExpressionPrinterImpl(ExpressionPrinterInt expressionPrinter) {
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
