package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpression;
import tools.mdsd.jamopp.model.java.expressions.MultiplicativeExpressionChild;
import tools.mdsd.jamopp.model.java.operators.MultiplicativeOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionPrinterImpl implements Printer<MultiplicativeExpression> {

	private final Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter;
	private final Printer<MultiplicativeOperator> multiplicativeOperatorPrinter;

	@Inject
	public MultiplicativeExpressionPrinterImpl(
			final Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter,
			final Printer<MultiplicativeOperator> multiplicativeOperatorPrinter) {
		this.multiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
		this.multiplicativeOperatorPrinter = multiplicativeOperatorPrinter;
	}

	@Override
	public void print(final MultiplicativeExpression element, final BufferedWriter writer) throws IOException {
		multiplicativeExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			multiplicativeOperatorPrinter.print(element.getMultiplicativeOperators().get(index - 1), writer);
			multiplicativeExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
