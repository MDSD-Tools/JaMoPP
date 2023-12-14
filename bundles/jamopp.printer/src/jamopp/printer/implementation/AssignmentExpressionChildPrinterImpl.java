package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AssignmentExpressionChildPrinterImpl implements Printer<AssignmentExpressionChild> {

	private final Printer<ConditionalExpressionChild> ConditionalExpressionChildPrinter;
	private final Printer<ConditionalExpression> ConditionalExpressionPrinter;

	@Inject
	public AssignmentExpressionChildPrinterImpl(Printer<ConditionalExpression> conditionalExpressionPrinter,
			Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter) {
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
