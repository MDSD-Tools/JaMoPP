package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionalAndExpressionPrinterInt;

class ConditionalAndExpressionPrinter implements Printer<ConditionalAndExpression>, ConditionalAndExpressionPrinterInt {

	private final ConditionalAndExpressionChildPrinter ConditionalAndExpressionChildPrinter;

	@Inject
	public ConditionalAndExpressionPrinter(
			jamopp.printer.implementation.ConditionalAndExpressionChildPrinter conditionalAndExpressionChildPrinter) {
		super();
		ConditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalAndExpression element, BufferedWriter writer) throws IOException {
		ConditionalAndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			ConditionalAndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
