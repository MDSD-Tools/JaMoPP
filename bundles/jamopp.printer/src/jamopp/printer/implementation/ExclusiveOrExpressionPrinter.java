package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExclusiveOrExpressionPrinterInt;

class ExclusiveOrExpressionPrinter implements ExclusiveOrExpressionPrinterInt {

	private final ExclusiveOrExpressionChildPrinter ExclusiveOrExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionPrinter(
			jamopp.printer.implementation.ExclusiveOrExpressionChildPrinter exclusiveOrExpressionChildPrinter) {
		super();
		ExclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(ExclusiveOrExpression element, BufferedWriter writer) throws IOException {
		ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
