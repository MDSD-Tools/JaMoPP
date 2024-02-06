package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InclusiveOrExpressionPrinterImpl implements Printer<InclusiveOrExpression> {

	private final Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter;

	@Inject
	public InclusiveOrExpressionPrinterImpl(
			final Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter) {
		this.inclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(final InclusiveOrExpression element, final BufferedWriter writer) throws IOException {
		inclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			inclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
