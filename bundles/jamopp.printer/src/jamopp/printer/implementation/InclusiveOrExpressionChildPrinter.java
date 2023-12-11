package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class InclusiveOrExpressionChildPrinter implements Printer<InclusiveOrExpressionChild> {

	private final ExclusiveOrExpressionPrinter ExclusiveOrExpressionPrinter;
	private final ExclusiveOrExpressionChildPrinter ExclusiveOrExpressionChildPrinter;

	@Inject
	public InclusiveOrExpressionChildPrinter(
			jamopp.printer.implementation.ExclusiveOrExpressionPrinter exclusiveOrExpressionPrinter,
			jamopp.printer.implementation.ExclusiveOrExpressionChildPrinter exclusiveOrExpressionChildPrinter) {
		super();
		ExclusiveOrExpressionPrinter = exclusiveOrExpressionPrinter;
		ExclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	public void print(InclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			ExclusiveOrExpressionPrinter.print((ExclusiveOrExpression) element, writer);
		} else {
			ExclusiveOrExpressionChildPrinter.print((ExclusiveOrExpressionChild) element, writer);
		}
	}

}
