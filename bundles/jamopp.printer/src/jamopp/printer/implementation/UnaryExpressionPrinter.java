package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.UnaryExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryExpressionPrinterInt;
import jamopp.printer.interfaces.printer.UnaryOperatorPrinterInt;

public class UnaryExpressionPrinter implements UnaryExpressionPrinterInt {

	private final UnaryOperatorPrinterInt UnaryOperatorPrinter;
	private final UnaryExpressionChildPrinterInt UnaryExpressionChildPrinter;

	@Inject
	public UnaryExpressionPrinter(UnaryOperatorPrinterInt unaryOperatorPrinter,
			UnaryExpressionChildPrinterInt unaryExpressionChildPrinter) {
		UnaryOperatorPrinter = unaryOperatorPrinter;
		UnaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(UnaryExpression element, BufferedWriter writer) throws IOException {
		for (UnaryOperator op : element.getOperators()) {
			UnaryOperatorPrinter.print(op, writer);
		}
		UnaryExpressionChildPrinter.print(element.getChild(), writer);
	}

	

}
