package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpression;
import tools.mdsd.jamopp.model.java.expressions.InclusiveOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalAndExpressionChildPrinterImpl implements Printer<ConditionalAndExpressionChild> {

	private final Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter;
	private final Printer<InclusiveOrExpression> inclusiveOrExpressionPrinter;

	@Inject
	public ConditionalAndExpressionChildPrinterImpl(final Printer<InclusiveOrExpression> inclusiveOrExpressionPrinter,
			final Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter) {
		this.inclusiveOrExpressionPrinter = inclusiveOrExpressionPrinter;
		this.inclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(final ConditionalAndExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof InclusiveOrExpression) {
			inclusiveOrExpressionPrinter.print((InclusiveOrExpression) element, writer);
		} else {
			inclusiveOrExpressionChildPrinter.print((InclusiveOrExpressionChild) element, writer);
		}
	}

}
