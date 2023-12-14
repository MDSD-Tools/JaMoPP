package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ConditionalAndExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.InclusiveOrExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.InclusiveOrExpressionPrinterInt;

public class ConditionalAndExpressionChildPrinterImpl implements Printer<ConditionalAndExpressionChild> {

	private final InclusiveOrExpressionPrinterInt InclusiveOrExpressionPrinter;
	private final InclusiveOrExpressionChildPrinterInt InclusiveOrExpressionChildPrinter;

	@Inject
	public ConditionalAndExpressionChildPrinterImpl(InclusiveOrExpressionPrinterInt inclusiveOrExpressionPrinter,
			InclusiveOrExpressionChildPrinterInt inclusiveOrExpressionChildPrinter) {
		InclusiveOrExpressionPrinter = inclusiveOrExpressionPrinter;
		InclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalAndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof InclusiveOrExpression) {
			InclusiveOrExpressionPrinter.print((InclusiveOrExpression) element, writer);
		} else {
			InclusiveOrExpressionChildPrinter.print((InclusiveOrExpressionChild) element, writer);
		}
	}

}
