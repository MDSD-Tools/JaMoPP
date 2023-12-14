package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ShiftExpressionChildPrinterImpl implements Printer<ShiftExpressionChild> {

	private final Printer<AdditiveExpressionChild> additiveExpressionChildPrinter;
	private final Printer<AdditiveExpression> additiveExpressionPrinter;

	@Inject
	public ShiftExpressionChildPrinterImpl(Printer<AdditiveExpression> additiveExpressionPrinter,
			Printer<AdditiveExpressionChild> additiveExpressionChildPrinter) {
		this.additiveExpressionPrinter = additiveExpressionPrinter;
		this.additiveExpressionChildPrinter = additiveExpressionChildPrinter;
	}

	@Override
	public void print(ShiftExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof AdditiveExpression) {
			this.additiveExpressionPrinter.print((AdditiveExpression) element, writer);
		} else {
			this.additiveExpressionChildPrinter.print((AdditiveExpressionChild) element, writer);
		}
	}

}
