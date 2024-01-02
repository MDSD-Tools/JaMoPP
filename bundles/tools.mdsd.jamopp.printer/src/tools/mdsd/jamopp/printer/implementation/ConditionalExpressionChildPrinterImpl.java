package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.ConditionalExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalExpressionChildPrinterImpl implements Printer<ConditionalExpressionChild> {

	private final Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter;
	private final Printer<ConditionalOrExpression> conditionalOrExpressionPrinter;

	@Inject
	public ConditionalExpressionChildPrinterImpl(Printer<ConditionalOrExpression> conditionalOrExpressionPrinter,
			Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter) {
		this.conditionalOrExpressionPrinter = conditionalOrExpressionPrinter;
		this.conditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalOrExpression) {
			this.conditionalOrExpressionPrinter.print((ConditionalOrExpression) element, writer);
		} else {
			this.conditionalOrExpressionChildPrinter.print((ConditionalOrExpressionChild) element, writer);
		}
	}

}
