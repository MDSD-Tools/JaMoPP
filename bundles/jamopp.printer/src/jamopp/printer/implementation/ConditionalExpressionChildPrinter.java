package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpression;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ConditionalExpressionChildPrinter implements Printer<ConditionalExpressionChild> {

	private final ConditionalOrExpressionPrinter ConditionalOrExpressionPrinter;
	private final ConditionalOrExpressionChildPrinter ConditionalOrExpressionChildPrinter;

	@Inject
	public ConditionalExpressionChildPrinter(
			jamopp.printer.implementation.ConditionalOrExpressionPrinter conditionalOrExpressionPrinter,
			jamopp.printer.implementation.ConditionalOrExpressionChildPrinter conditionalOrExpressionChildPrinter) {
		super();
		ConditionalOrExpressionPrinter = conditionalOrExpressionPrinter;
		ConditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	public void print(ConditionalExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalOrExpression) {
			ConditionalOrExpressionPrinter.print((ConditionalOrExpression) element, writer);
		} else {
			ConditionalOrExpressionChildPrinter.print((ConditionalOrExpressionChild) element, writer);
		}
	}

}
