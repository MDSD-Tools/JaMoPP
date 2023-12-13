package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalOrExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionalOrExpressionPrinterInt;

public class ConditionalOrExpressionPrinter implements ConditionalOrExpressionPrinterInt {

	private final ConditionalOrExpressionChildPrinter ConditionalOrExpressionChildPrinter;

	@Inject
	public ConditionalOrExpressionPrinter(
			jamopp.printer.implementation.ConditionalOrExpressionChildPrinter conditionalOrExpressionChildPrinter) {
		super();
		ConditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalOrExpression element, BufferedWriter writer) throws IOException {
		ConditionalOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" || ");
			ConditionalOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
