package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditiveExpressionPrinterInt;

class AdditiveExpressionPrinter implements AdditiveExpressionPrinterInt {

	private final AdditiveExpressionChildPrinter AdditiveExpressionChildPrinter;
	private final AdditiveOperatorPrinter AdditiveOperatorPrinter;

	@Inject
	public AdditiveExpressionPrinter(
			jamopp.printer.implementation.AdditiveExpressionChildPrinter additiveExpressionChildPrinter,
			jamopp.printer.implementation.AdditiveOperatorPrinter additiveOperatorPrinter) {
		super();
		AdditiveExpressionChildPrinter = additiveExpressionChildPrinter;
		AdditiveOperatorPrinter = additiveOperatorPrinter;
	}

	@Override
	public void print(AdditiveExpression element, BufferedWriter writer) throws IOException {
		AdditiveExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			AdditiveOperatorPrinter.print(element.getAdditiveOperators().get(index - 1), writer);
			AdditiveExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
