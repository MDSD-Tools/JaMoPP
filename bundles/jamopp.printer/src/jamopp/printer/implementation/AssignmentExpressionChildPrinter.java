package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AssignmentExpressionChildPrinterInt;

class AssignmentExpressionChildPrinter implements Printer<AssignmentExpressionChild>, AssignmentExpressionChildPrinterInt {

	private final ConditionalExpressionPrinter ConditionalExpressionPrinter;
	private final ConditionalExpressionChildPrinter ConditionalExpressionChildPrinter;

	@Inject
	public AssignmentExpressionChildPrinter(
			jamopp.printer.implementation.ConditionalExpressionPrinter conditionalExpressionPrinter,
			jamopp.printer.implementation.ConditionalExpressionChildPrinter conditionalExpressionChildPrinter) {
		super();
		ConditionalExpressionPrinter = conditionalExpressionPrinter;
		ConditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
	}

	@Override
	public void print(AssignmentExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalExpression) {
			ConditionalExpressionPrinter.print((ConditionalExpression) element, writer);
		} else {
			ConditionalExpressionChildPrinter.print((ConditionalExpressionChild) element, writer);
		}
	}

}
