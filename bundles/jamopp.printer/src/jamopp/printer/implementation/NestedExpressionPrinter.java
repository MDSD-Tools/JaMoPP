package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.NestedExpression;

import jamopp.printer.interfaces.Printer;

class NestedExpressionPrinter implements Printer<NestedExpression>{

	private final ExpressionPrinter ExpressionPrinter;
	
	public void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
