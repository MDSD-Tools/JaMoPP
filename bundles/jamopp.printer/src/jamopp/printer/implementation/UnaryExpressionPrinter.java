package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.operators.UnaryOperator;

import jamopp.printer.interfaces.Printer;

class UnaryExpressionPrinter implements Printer<UnaryExpression>{

	public void print(UnaryExpression element, BufferedWriter writer) throws IOException {
		for (UnaryOperator op : element.getOperators()) {
			UnaryOperatorPrinter.print(op, writer);
		}
		UnaryExpressionChildPrinter.print(element.getChild(), writer);
	}

}
