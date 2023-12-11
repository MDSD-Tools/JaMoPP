package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.NestedExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class NestedExpressionPrinter implements Printer<NestedExpression> {

	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public NestedExpressionPrinter(jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ExpressionPrinter = expressionPrinter;
	}

	public void print(NestedExpression element, BufferedWriter writer) throws IOException {
		writer.append("(");
		ExpressionPrinter.print(element.getExpression(), writer);
		writer.append(")");
	}

}
