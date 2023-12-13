package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ShiftExpressionChildPrinterInt;

class ShiftExpressionChildPrinter implements ShiftExpressionChildPrinterInt {

	private final AdditiveExpressionPrinter AdditiveExpressionPrinter;
	private final AdditiveExpressionChildPrinter AdditiveExpressionChildPrinter;

	@Inject
	public ShiftExpressionChildPrinter(
			AdditiveExpressionPrinter additiveExpressionPrinter,
			AdditiveExpressionChildPrinter additiveExpressionChildPrinter) {
		super();
		AdditiveExpressionPrinter = additiveExpressionPrinter;
		AdditiveExpressionChildPrinter = additiveExpressionChildPrinter;
	}

	@Override
	public void print(ShiftExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof AdditiveExpression) {
			AdditiveExpressionPrinter.print((AdditiveExpression) element, writer);
		} else {
			AdditiveExpressionChildPrinter.print((AdditiveExpressionChild) element, writer);
		}
	}

}
