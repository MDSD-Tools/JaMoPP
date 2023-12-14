package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionChildPrinterImpl implements Printer<ConditionalOrExpressionChild> {

	private final Printer<ConditionalAndExpressionChild> ConditionalAndExpressionChildPrinter;
	private final Printer<ConditionalAndExpression> ConditionalAndExpressionPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinterImpl(Printer<ConditionalAndExpression> conditionalAndExpressionPrinter,
			Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
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
