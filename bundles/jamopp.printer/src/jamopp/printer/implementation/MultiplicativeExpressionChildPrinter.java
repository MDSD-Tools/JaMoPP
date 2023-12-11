package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import jamopp.printer.interfaces.Printer;

class MultiplicativeExpressionChildPrinter implements Printer<MultiplicativeExpressionChild>{

	private final UnaryExpressionPrinter UnaryExpressionPrinter;
	private final UnaryExpressionChildPrinter UnaryExpressionChildPrinter;
	
	public void print(MultiplicativeExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
