package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssignmentExpressionChildPrinterImpl implements Printer<AssignmentExpressionChild> {

	private final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter;
	private final Printer<ConditionalExpression> conditionalExpressionPrinter;

	@Inject
	public AssignmentExpressionChildPrinterImpl(final Printer<ConditionalExpression> conditionalExpressionPrinter,
			final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter) {
		this.conditionalExpressionPrinter = conditionalExpressionPrinter;
		this.conditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
	}

	@Override
	public void print(final AssignmentExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalExpression) {
			conditionalExpressionPrinter.print((ConditionalExpression) element, writer);
		} else {
			conditionalExpressionChildPrinter.print((ConditionalExpressionChild) element, writer);
		}
	}

}
