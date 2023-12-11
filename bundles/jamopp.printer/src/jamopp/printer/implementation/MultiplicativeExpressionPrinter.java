package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class MultiplicativeExpressionPrinter implements Printer<MultiplicativeExpression> {

	private final MultiplicativeExpressionChildPrinter MultiplicativeExpressionChildPrinter;
	private final MultiplicativeOperatorPrinter MultiplicativeOperatorPrinter;

	@Inject
	public MultiplicativeExpressionPrinter(
			jamopp.printer.implementation.MultiplicativeExpressionChildPrinter multiplicativeExpressionChildPrinter,
			jamopp.printer.implementation.MultiplicativeOperatorPrinter multiplicativeOperatorPrinter) {
		super();
		MultiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
		MultiplicativeOperatorPrinter = multiplicativeOperatorPrinter;
	}

	public void print(MultiplicativeExpression element, BufferedWriter writer) throws IOException {
		MultiplicativeExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			MultiplicativeOperatorPrinter.print(element.getMultiplicativeOperators().get(index - 1), writer);
			MultiplicativeExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
