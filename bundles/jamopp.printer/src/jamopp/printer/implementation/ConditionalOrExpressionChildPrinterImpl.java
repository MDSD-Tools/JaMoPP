package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ConditionalAndExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalAndExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ConditionalOrExpressionChildPrinterInt;

public class ConditionalOrExpressionChildPrinterImpl implements ConditionalOrExpressionChildPrinterInt {

	private final ConditionalAndExpressionPrinterInt ConditionalAndExpressionPrinter;
	private final ConditionalAndExpressionChildPrinterInt ConditionalAndExpressionChildPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinterImpl(ConditionalAndExpressionPrinterInt conditionalAndExpressionPrinter,
			ConditionalAndExpressionChildPrinterInt conditionalAndExpressionChildPrinter) {
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
