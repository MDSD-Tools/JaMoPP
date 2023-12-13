package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AdditiveExpressionChildPrinter implements  AdditiveExpressionChildPrinterInt {

	private final MultiplicativeExpressionPrinter MultiplicativeExpressionPrinter;
	private final MultiplicativeExpressionChildPrinter MultiplicativeExpressionChildPrinter;

	@Inject
	public AdditiveExpressionChildPrinter(
			jamopp.printer.implementation.MultiplicativeExpressionPrinter multiplicativeExpressionPrinter,
			jamopp.printer.implementation.MultiplicativeExpressionChildPrinter multiplicativeExpressionChildPrinter) {
		super();
		MultiplicativeExpressionPrinter = multiplicativeExpressionPrinter;
		MultiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
	}

	@Override
	public void print(AdditiveExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof MultiplicativeExpression) {
			MultiplicativeExpressionPrinter.print((MultiplicativeExpression) element, writer);
		} else {
			MultiplicativeExpressionChildPrinter.print((MultiplicativeExpressionChild) element, writer);
		}
	}

}
