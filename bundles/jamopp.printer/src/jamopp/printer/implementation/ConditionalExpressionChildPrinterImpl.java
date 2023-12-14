package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpression;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionalExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalOrExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalOrExpressionPrinterInt;

public class ConditionalExpressionChildPrinterImpl implements Printer<ConditionalExpressionChild> {

	private final Printer<ConditionalOrExpression> ConditionalOrExpressionPrinter;
	private final Printer<ConditionalOrExpressionChild> ConditionalOrExpressionChildPrinter;

	@Inject
	public ConditionalExpressionChildPrinterImpl(Printer<ConditionalOrExpression> conditionalOrExpressionPrinter,
			Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter) {
		ConditionalOrExpressionPrinter = conditionalOrExpressionPrinter;
		ConditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalOrExpression) {
			ConditionalOrExpressionPrinter.print((ConditionalOrExpression) element, writer);
		} else {
			ConditionalOrExpressionChildPrinter.print((ConditionalOrExpressionChild) element, writer);
		}
	}

}
