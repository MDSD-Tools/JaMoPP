package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ExclusiveOrExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ExclusiveOrExpressionPrinterInt;
import jamopp.printer.interfaces.printer.InclusiveOrExpressionChildPrinterInt;

public class InclusiveOrExpressionChildPrinterImpl implements InclusiveOrExpressionChildPrinterInt {

	private final ExclusiveOrExpressionPrinterInt ExclusiveOrExpressionPrinter;
	private final ExclusiveOrExpressionChildPrinterInt ExclusiveOrExpressionChildPrinter;

	@Inject
	public InclusiveOrExpressionChildPrinterImpl(ExclusiveOrExpressionPrinterInt exclusiveOrExpressionPrinter,
			ExclusiveOrExpressionChildPrinterInt exclusiveOrExpressionChildPrinter) {
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
