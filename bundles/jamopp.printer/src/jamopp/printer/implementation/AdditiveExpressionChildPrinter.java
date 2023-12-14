package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditiveExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.MultiplicativeExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.MultiplicativeExpressionPrinterInt;

public class AdditiveExpressionChildPrinter implements AdditiveExpressionChildPrinterInt {

	private final MultiplicativeExpressionPrinterInt MultiplicativeExpressionPrinter;
	private final MultiplicativeExpressionChildPrinterInt MultiplicativeExpressionChildPrinter;

	@Inject
	public AdditiveExpressionChildPrinter(MultiplicativeExpressionPrinterInt multiplicativeExpressionPrinter,
			MultiplicativeExpressionChildPrinterInt multiplicativeExpressionChildPrinter) {
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
