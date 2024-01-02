package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.UnaryExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryOperator;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryExpressionPrinterImpl implements Printer<UnaryExpression> {

	private final Printer<UnaryExpressionChild> unaryExpressionChildPrinter;
	private final Printer<UnaryOperator> unaryOperatorPrinter;

	@Inject
	public UnaryExpressionPrinterImpl(Printer<UnaryOperator> unaryOperatorPrinter,
			Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		this.unaryOperatorPrinter = unaryOperatorPrinter;
		this.unaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(UnaryExpression element, BufferedWriter writer) throws IOException {
		for (UnaryOperator op : element.getOperators()) {
			this.unaryOperatorPrinter.print(op, writer);
		}
		this.unaryExpressionChildPrinter.print(element.getChild(), writer);
	}

}
