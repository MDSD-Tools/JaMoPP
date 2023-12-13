package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionalExpressionPrinterInt;

class ConditionalExpressionPrinter implements ConditionalExpressionPrinterInt {

	private final ConditionalExpressionChildPrinter ConditionalExpressionChildPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public ConditionalExpressionPrinter(
			jamopp.printer.implementation.ConditionalExpressionChildPrinter conditionalExpressionChildPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		ConditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(ConditionalExpression element, BufferedWriter writer) throws IOException {
		ConditionalExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getExpressionIf() != null) {
			writer.append(" ? ");
			ExpressionPrinter.print(element.getExpressionIf(), writer);
			writer.append(" : ");
			ExpressionPrinter.print(element.getGeneralExpressionElse(), writer);
		}
	}

}
