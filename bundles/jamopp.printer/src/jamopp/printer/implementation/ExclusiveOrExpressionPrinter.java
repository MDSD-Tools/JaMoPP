package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;

import jamopp.printer.interfaces.Printer;

class ExclusiveOrExpressionPrinter implements Printer<ExclusiveOrExpression>{

	private final ExclusiveOrExpressionChildPrinter ExclusiveOrExpressionChildPrinter;
	
	public void print(ExclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
