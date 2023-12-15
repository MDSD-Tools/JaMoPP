package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InclusiveOrExpressionChildPrinterImpl implements Printer<InclusiveOrExpressionChild> {

	private final Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter;
	private final Printer<ExclusiveOrExpression> exclusiveOrExpressionPrinter;

	@Inject
	public InclusiveOrExpressionChildPrinterImpl(Printer<ExclusiveOrExpression> exclusiveOrExpressionPrinter,
			Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter) {
		this.exclusiveOrExpressionPrinter = exclusiveOrExpressionPrinter;
		this.exclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(InclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ExclusiveOrExpression) {
			this.exclusiveOrExpressionPrinter.print((ExclusiveOrExpression) element, writer);
		} else {
			this.exclusiveOrExpressionChildPrinter.print((ExclusiveOrExpressionChild) element, writer);
		}
	}

}