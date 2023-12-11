package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InclusiveOrExpression;

import jamopp.printer.interfaces.Printer;

class InclusiveOrExpressionPrinter implements Printer<InclusiveOrExpression>{

	public void print(InclusiveOrExpression element, BufferedWriter writer)
			throws IOException {
		InclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			InclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
