package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;

import jamopp.printer.interfaces.Printer;

class ConditionalExpressionPrinter implements Printer<ConditionalExpression> {

	private final ConditionalExpressionChildPrinter ConditionalExpressionChildPrinter;
	private final ExpressionPrinter ExpressionPrinter;
	
	public void print(ConditionalExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			ExpressionPrinter.print(element.getExpressionIf(), writer);
			writer.append(" : ");
			ExpressionPrinter.print(element.getGeneralExpressionElse(), writer);
		}
	}

}
