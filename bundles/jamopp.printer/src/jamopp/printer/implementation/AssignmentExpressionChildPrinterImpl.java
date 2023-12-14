package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.AssignmentExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalExpressionPrinterInt;

public class AssignmentExpressionChildPrinterImpl implements AssignmentExpressionChildPrinterInt {

	private final ConditionalExpressionPrinterInt ConditionalExpressionPrinter;
	private final ConditionalExpressionChildPrinterInt ConditionalExpressionChildPrinter;

	@Inject
	public AssignmentExpressionChildPrinterImpl(ConditionalExpressionPrinterInt conditionalExpressionPrinter,
			ConditionalExpressionChildPrinterInt conditionalExpressionChildPrinter) {
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
