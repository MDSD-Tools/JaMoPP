package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.printer.MultiplicativeExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.MultiplicativeExpressionPrinterInt;
import jamopp.printer.interfaces.printer.MultiplicativeOperatorPrinterInt;

public class MultiplicativeExpressionPrinter implements MultiplicativeExpressionPrinterInt {

	private final MultiplicativeExpressionChildPrinterInt MultiplicativeExpressionChildPrinter;
	private final MultiplicativeOperatorPrinterInt MultiplicativeOperatorPrinter;

	@Inject
	public MultiplicativeExpressionPrinter(MultiplicativeExpressionChildPrinterInt multiplicativeExpressionChildPrinter,
			MultiplicativeOperatorPrinterInt multiplicativeOperatorPrinter) {
		MultiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
		MultiplicativeOperatorPrinter = multiplicativeOperatorPrinter;
	}

	@Override
	public void print(MultiplicativeExpression element, BufferedWriter writer) throws IOException {
		MultiplicativeExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			MultiplicativeOperatorPrinter.print(element.getMultiplicativeOperators().get(index - 1), writer);
			MultiplicativeExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
