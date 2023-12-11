package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class UnaryExpressionPrinter implements Printer<UnaryExpression> {

	private final UnaryOperatorPrinter UnaryOperatorPrinter;
	private final UnaryExpressionChildPrinter UnaryExpressionChildPrinter;

	@Inject
	public UnaryExpressionPrinter(jamopp.printer.implementation.UnaryOperatorPrinter unaryOperatorPrinter,
			jamopp.printer.implementation.UnaryExpressionChildPrinter unaryExpressionChildPrinter) {
		super();
		UnaryOperatorPrinter = unaryOperatorPrinter;
		UnaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	public void print(UnaryExpression element, BufferedWriter writer) throws IOException {
		for (UnaryOperator op : element.getOperators()) {
			UnaryOperatorPrinter.print(op, writer);
		}
		UnaryExpressionChildPrinter.print(element.getChild(), writer);
	}

}
