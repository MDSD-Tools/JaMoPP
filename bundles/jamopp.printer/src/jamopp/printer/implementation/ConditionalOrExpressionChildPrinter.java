package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ConditionalOrExpressionChildPrinter implements Printer<ConditionalOrExpressionChild> {

	private final ConditionalAndExpressionPrinter ConditionalAndExpressionPrinter;
	private final ConditionalAndExpressionChildPrinter ConditionalAndExpressionChildPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinter(
			jamopp.printer.implementation.ConditionalAndExpressionPrinter conditionalAndExpressionPrinter,
			jamopp.printer.implementation.ConditionalAndExpressionChildPrinter conditionalAndExpressionChildPrinter) {
		super();
		ConditionalAndExpressionPrinter = conditionalAndExpressionPrinter;
		ConditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalAndExpression) {
			ConditionalAndExpressionPrinter.print((ConditionalAndExpression) element, writer);
		} else {
			ConditionalAndExpressionChildPrinter.print((ConditionalAndExpressionChild) element, writer);
		}
	}

}
