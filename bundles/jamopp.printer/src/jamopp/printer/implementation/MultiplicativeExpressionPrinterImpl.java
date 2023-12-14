package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.operators.MultiplicativeOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionPrinterImpl implements Printer<MultiplicativeExpression> {

	private final Printer<MultiplicativeExpressionChild> MultiplicativeExpressionChildPrinter;
	private final Printer<MultiplicativeOperator> MultiplicativeOperatorPrinter;

	@Inject
	public MultiplicativeExpressionPrinterImpl(Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter,
			Printer<MultiplicativeOperator> multiplicativeOperatorPrinter) {
		MultiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
		MultiplicativeOperatorPrinter = multiplicativeOperatorPrinter;
	}

	@Override
	public void print(MultiplicativeExpression element, BufferedWriter writer) throws IOException {
		MultiplicativeExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			MultiplicativeOperatorPrinter.print(element.getMultiplicativeOperators().get(index - 1), writer);
			MultiplicativeExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
