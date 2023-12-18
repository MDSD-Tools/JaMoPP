package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AdditiveExpressionChildPrinterImpl implements Printer<AdditiveExpressionChild> {

	private final Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter;
	private final Printer<MultiplicativeExpression> multiplicativeExpressionPrinter;

	@Inject
	public AdditiveExpressionChildPrinterImpl(Printer<MultiplicativeExpression> multiplicativeExpressionPrinter,
			Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter) {
		this.multiplicativeExpressionPrinter = multiplicativeExpressionPrinter;
		this.multiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
	}

	@Override
	public void print(AdditiveExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof MultiplicativeExpression) {
			this.multiplicativeExpressionPrinter.print((MultiplicativeExpression) element, writer);
		} else {
			this.multiplicativeExpressionChildPrinter.print((MultiplicativeExpressionChild) element, writer);
		}
	}

}