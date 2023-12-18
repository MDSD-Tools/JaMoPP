package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionChildPrinterImpl implements Printer<ConditionalOrExpressionChild> {

	private final Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter;
	private final Printer<ConditionalAndExpression> conditionalAndExpressionPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinterImpl(Printer<ConditionalAndExpression> conditionalAndExpressionPrinter,
			Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
		this.conditionalAndExpressionPrinter = conditionalAndExpressionPrinter;
		this.conditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalAndExpression) {
			this.conditionalAndExpressionPrinter.print((ConditionalAndExpression) element, writer);
		} else {
			this.conditionalAndExpressionChildPrinter.print((ConditionalAndExpressionChild) element, writer);
		}
	}

}
