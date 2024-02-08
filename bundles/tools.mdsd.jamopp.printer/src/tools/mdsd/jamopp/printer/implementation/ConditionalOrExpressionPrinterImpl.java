package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionPrinterImpl implements Printer<ConditionalOrExpression> {

	private final Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter;

	@Inject
	public ConditionalOrExpressionPrinterImpl(
			final Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter) {
		this.conditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	@Override
	public void print(final ConditionalOrExpression element, final BufferedWriter writer) throws IOException {
		conditionalOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" || ");
			conditionalOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
