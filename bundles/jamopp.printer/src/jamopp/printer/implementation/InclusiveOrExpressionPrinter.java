package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InclusiveOrExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.InclusiveOrExpressionPrinterInt;

class InclusiveOrExpressionPrinter implements InclusiveOrExpressionPrinterInt {

	private final InclusiveOrExpressionChildPrinter InclusiveOrExpressionChildPrinter;

	@Inject
	public InclusiveOrExpressionPrinter(
			jamopp.printer.implementation.InclusiveOrExpressionChildPrinter inclusiveOrExpressionChildPrinter) {
		super();
		InclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(InclusiveOrExpression element, BufferedWriter writer) throws IOException {
		InclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			InclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
