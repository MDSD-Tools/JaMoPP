package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;

import jamopp.printer.interfaces.Printer;

class ConditionalAndExpressionPrinter implements Printer<ConditionalAndExpression> {

	public void print(ConditionalAndExpression element, BufferedWriter writer)
			throws IOException {
		ConditionalAndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			ConditionalAndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
