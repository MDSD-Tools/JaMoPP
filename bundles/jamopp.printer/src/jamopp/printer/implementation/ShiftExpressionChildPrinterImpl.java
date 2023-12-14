package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditiveExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AdditiveExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ShiftExpressionChildPrinterInt;

public class ShiftExpressionChildPrinterImpl implements ShiftExpressionChildPrinterInt {

	private final Printer<AdditiveExpression> AdditiveExpressionPrinter;
	private final Printer<AdditiveExpressionChild> AdditiveExpressionChildPrinter;

	@Inject
	public ShiftExpressionChildPrinterImpl(Printer<AdditiveExpression> additiveExpressionPrinter,
			Printer<AdditiveExpressionChild> additiveExpressionChildPrinter) {
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
