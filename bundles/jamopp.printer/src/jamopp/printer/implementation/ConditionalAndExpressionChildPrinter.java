package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ConditionalAndExpressionChildPrinter implements Printer<ConditionalAndExpressionChild> {

	private final InclusiveOrExpressionPrinter InclusiveOrExpressionPrinter;
	private final InclusiveOrExpressionChildPrinter InclusiveOrExpressionChildPrinter;

	@Inject
	public ConditionalAndExpressionChildPrinter(
			jamopp.printer.implementation.InclusiveOrExpressionPrinter inclusiveOrExpressionPrinter,
			jamopp.printer.implementation.InclusiveOrExpressionChildPrinter inclusiveOrExpressionChildPrinter) {
		super();
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
