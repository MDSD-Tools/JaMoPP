package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExclusiveOrExpressionPrinterImpl implements Printer<ExclusiveOrExpression> {

	private final Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionPrinterImpl(
			final Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter) {
		this.exclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(final ExclusiveOrExpression element, final BufferedWriter writer) throws IOException {
		exclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			exclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
