package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConditionalAndExpressionChildPrinterImpl implements Printer<ConditionalAndExpressionChild> {

	private final Printer<InclusiveOrExpressionChild> InclusiveOrExpressionChildPrinter;
	private final Printer<InclusiveOrExpression> InclusiveOrExpressionPrinter;

	@Inject
	public ConditionalAndExpressionChildPrinterImpl(Printer<InclusiveOrExpression> inclusiveOrExpressionPrinter,
			Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter) {
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
