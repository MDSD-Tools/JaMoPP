package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.MultiplicativeExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryExpressionPrinterInt;

public class MultiplicativeExpressionChildPrinter implements MultiplicativeExpressionChildPrinterInt {

	private final UnaryExpressionPrinterInt UnaryExpressionPrinter;
	private final UnaryExpressionChildPrinterInt UnaryExpressionChildPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinter(UnaryExpressionPrinterInt unaryExpressionPrinter,
			UnaryExpressionChildPrinterInt unaryExpressionChildPrinter) {
		UnaryExpressionPrinter = unaryExpressionPrinter;
		UnaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(MultiplicativeExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
