package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExclusiveOrExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ExclusiveOrExpressionPrinterInt;

public class ExclusiveOrExpressionPrinter implements ExclusiveOrExpressionPrinterInt {

	private final ExclusiveOrExpressionChildPrinterInt ExclusiveOrExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionPrinter(ExclusiveOrExpressionChildPrinterInt exclusiveOrExpressionChildPrinter) {
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
