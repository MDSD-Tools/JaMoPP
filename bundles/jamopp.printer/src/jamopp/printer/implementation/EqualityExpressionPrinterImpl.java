package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.EqualityExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.EqualityExpressionPrinterInt;
import jamopp.printer.interfaces.printer.EqualityOperatorPrinterInt;

public class EqualityExpressionPrinterImpl implements EqualityExpressionPrinterInt {

	private final EqualityExpressionChildPrinterInt EqualityExpressionChildPrinter;
	private final EqualityOperatorPrinterInt EqualityOperatorPrinter;

	@Inject
	public EqualityExpressionPrinterImpl(EqualityExpressionChildPrinterInt equalityExpressionChildPrinter,
			EqualityOperatorPrinterInt equalityOperatorPrinter) {
		EqualityExpressionChildPrinter = equalityExpressionChildPrinter;
		EqualityOperatorPrinter = equalityOperatorPrinter;
	}

	@Override
	public void print(EqualityExpression element, BufferedWriter writer) throws IOException {
		EqualityExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			EqualityOperatorPrinter.print(element.getEqualityOperators().get(index - 1), writer);
			EqualityExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
