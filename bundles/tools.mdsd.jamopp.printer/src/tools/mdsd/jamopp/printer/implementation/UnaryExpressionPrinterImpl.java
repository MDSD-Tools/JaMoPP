package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryExpressionPrinterImpl implements Printer<UnaryExpression> {

	private final Printer<UnaryExpressionChild> unaryExpressionChildPrinter;
	private final Printer<UnaryOperator> unaryOperatorPrinter;

	@Inject
	public UnaryExpressionPrinterImpl(final Printer<UnaryOperator> unaryOperatorPrinter,
			final Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		this.unaryOperatorPrinter = unaryOperatorPrinter;
		this.unaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(final UnaryExpression element, final BufferedWriter writer) throws IOException {
		for (final UnaryOperator op : element.getOperators()) {
			unaryOperatorPrinter.print(op, writer);
		}
		unaryExpressionChildPrinter.print(element.getChild(), writer);
	}

}
