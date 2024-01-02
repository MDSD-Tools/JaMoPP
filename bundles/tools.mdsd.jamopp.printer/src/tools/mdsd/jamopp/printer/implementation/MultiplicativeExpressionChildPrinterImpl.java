package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionChildPrinterImpl implements Printer<MultiplicativeExpressionChild> {

	private final Printer<UnaryExpressionChild> unaryExpressionChildPrinter;
	private final Printer<UnaryExpression> unaryExpressionPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinterImpl(Printer<UnaryExpression> unaryExpressionPrinter,
			Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		this.unaryExpressionPrinter = unaryExpressionPrinter;
		this.unaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(MultiplicativeExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			this.unaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			this.unaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
