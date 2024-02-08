package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionChildPrinterImpl implements Printer<ConditionalOrExpressionChild> {

	private final Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter;
	private final Printer<ConditionalAndExpression> conditionalAndExpressionPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinterImpl(
			final Printer<ConditionalAndExpression> conditionalAndExpressionPrinter,
			final Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
		this.conditionalAndExpressionPrinter = conditionalAndExpressionPrinter;
		this.conditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(final ConditionalOrExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalAndExpression) {
			conditionalAndExpressionPrinter.print((ConditionalAndExpression) element, writer);
		} else {
			conditionalAndExpressionChildPrinter.print((ConditionalAndExpressionChild) element, writer);
		}
	}

}
