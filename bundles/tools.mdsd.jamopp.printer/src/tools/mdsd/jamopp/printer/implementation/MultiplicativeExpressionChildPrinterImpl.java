package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionChildPrinterImpl implements Printer<MultiplicativeExpressionChild> {

	private final Printer<UnaryExpressionChild> unaryExpressionChildPrinter;
	private final Printer<UnaryExpression> unaryExpressionPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinterImpl(final Printer<UnaryExpression> unaryExpressionPrinter,
			final Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		this.unaryExpressionPrinter = unaryExpressionPrinter;
		this.unaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(final MultiplicativeExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			unaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			unaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
