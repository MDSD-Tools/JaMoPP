package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssignmentExpressionChildPrinterImpl implements Printer<AssignmentExpressionChild> {

	private final Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter;
	private final Printer<ConditionalExpression> conditionalExpressionPrinter;

	@Inject
	public AssignmentExpressionChildPrinterImpl(Printer<ConditionalExpression> conditionalExpressionPrinter,
			Printer<ConditionalExpressionChild> conditionalExpressionChildPrinter) {
		this.conditionalExpressionPrinter = conditionalExpressionPrinter;
		this.conditionalExpressionChildPrinter = conditionalExpressionChildPrinter;
	}

	@Override
	public void print(AssignmentExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalExpression) {
			this.conditionalExpressionPrinter.print((ConditionalExpression) element, writer);
		} else {
			this.conditionalExpressionChildPrinter.print((ConditionalExpressionChild) element, writer);
		}
	}

}
