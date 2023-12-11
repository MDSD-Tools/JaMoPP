package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import jamopp.printer.interfaces.Printer;

class ConditionalAndExpressionChildPrinter implements Printer<ConditionalAndExpressionChild>{

	private final InclusiveOrExpressionPrinter InclusiveOrExpressionPrinter;
	private final InclusiveOrExpressionChildPrinter InclusiveOrExpressionChildPrinter;
	
	public void print(ConditionalAndExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof InclusiveOrExpression) {
			InclusiveOrExpressionPrinter.print((InclusiveOrExpression) element, writer);
		} else {
			InclusiveOrExpressionChildPrinter.print((InclusiveOrExpressionChild) element, writer);
		}
	}

}
