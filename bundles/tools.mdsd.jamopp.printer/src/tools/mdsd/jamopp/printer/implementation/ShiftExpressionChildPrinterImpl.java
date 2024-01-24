package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ShiftExpressionChildPrinterImpl implements Printer<ShiftExpressionChild> {

	private final Printer<AdditiveExpressionChild> additiveExpressionChildPrinter;
	private final Printer<AdditiveExpression> additiveExpressionPrinter;

	@Inject
	public ShiftExpressionChildPrinterImpl(final Printer<AdditiveExpression> additiveExpressionPrinter,
			final Printer<AdditiveExpressionChild> additiveExpressionChildPrinter) {
		this.additiveExpressionPrinter = additiveExpressionPrinter;
		this.additiveExpressionChildPrinter = additiveExpressionChildPrinter;
	}

	@Override
	public void print(final ShiftExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof AdditiveExpression) {
			additiveExpressionPrinter.print((AdditiveExpression) element, writer);
		} else {
			additiveExpressionChildPrinter.print((AdditiveExpressionChild) element, writer);
		}
	}

}
