package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ConditionalExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class ConditionalExpressionPrinterImpl implements ConditionalExpressionPrinterInt {

	private final ConditionalExpressionChildPrinterInt ConditionalExpressionChildPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public ConditionalExpressionPrinterImpl(ConditionalExpressionChildPrinterInt conditionalExpressionChildPrinter,
			ExpressionPrinterInt expressionPrinter) {
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
