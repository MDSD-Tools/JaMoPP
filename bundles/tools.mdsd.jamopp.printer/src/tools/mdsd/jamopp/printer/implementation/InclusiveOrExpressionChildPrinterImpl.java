package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InclusiveOrExpressionChildPrinterImpl implements Printer<InclusiveOrExpressionChild> {

	private final Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter;
	private final Printer<ExclusiveOrExpression> exclusiveOrExpressionPrinter;

	@Inject
	public InclusiveOrExpressionChildPrinterImpl(final Printer<ExclusiveOrExpression> exclusiveOrExpressionPrinter,
			final Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter) {
		this.exclusiveOrExpressionPrinter = exclusiveOrExpressionPrinter;
		this.exclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(final InclusiveOrExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			exclusiveOrExpressionPrinter.print((ExclusiveOrExpression) element, writer);
		} else {
			exclusiveOrExpressionChildPrinter.print((ExclusiveOrExpressionChild) element, writer);
		}
	}

}
