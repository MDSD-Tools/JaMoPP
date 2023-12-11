package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class MultiplicativeExpressionChildPrinter implements Printer<MultiplicativeExpressionChild> {

	private final UnaryExpressionPrinter UnaryExpressionPrinter;
	private final UnaryExpressionChildPrinter UnaryExpressionChildPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinter(
			jamopp.printer.implementation.UnaryExpressionPrinter unaryExpressionPrinter,
			jamopp.printer.implementation.UnaryExpressionChildPrinter unaryExpressionChildPrinter) {
		super();
		UnaryExpressionPrinter = unaryExpressionPrinter;
		UnaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	public void print(MultiplicativeExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
