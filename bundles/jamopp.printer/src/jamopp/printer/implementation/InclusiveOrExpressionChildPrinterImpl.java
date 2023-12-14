package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InclusiveOrExpressionChildPrinterImpl implements Printer<InclusiveOrExpressionChild> {

	private final Printer<ExclusiveOrExpressionChild> ExclusiveOrExpressionChildPrinter;
	private final Printer<ExclusiveOrExpression> ExclusiveOrExpressionPrinter;

	@Inject
	public InclusiveOrExpressionChildPrinterImpl(Printer<ExclusiveOrExpression> exclusiveOrExpressionPrinter,
			Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter) {
		ExclusiveOrExpressionPrinter = exclusiveOrExpressionPrinter;
		ExclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(InclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			ExclusiveOrExpressionPrinter.print((ExclusiveOrExpression) element, writer);
		} else {
			ExclusiveOrExpressionChildPrinter.print((ExclusiveOrExpressionChild) element, writer);
		}
	}

}
