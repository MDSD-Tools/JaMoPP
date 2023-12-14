package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.operators.UnaryOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class UnaryExpressionPrinterImpl implements Printer<UnaryExpression> {

	private final Printer<UnaryExpressionChild> UnaryExpressionChildPrinter;
	private final Printer<UnaryOperator> UnaryOperatorPrinter;

	@Inject
	public UnaryExpressionPrinterImpl(Printer<UnaryOperator> unaryOperatorPrinter,
			Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
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
