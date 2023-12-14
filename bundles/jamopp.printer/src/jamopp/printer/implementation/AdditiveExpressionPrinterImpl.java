package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.operators.AdditiveOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AdditiveExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AdditiveExpressionPrinterInt;
import jamopp.printer.interfaces.printer.AdditiveOperatorPrinterInt;

public class AdditiveExpressionPrinterImpl implements Printer<AdditiveExpression> {

	private final Printer<AdditiveExpressionChild> AdditiveExpressionChildPrinter;
	private final Printer<AdditiveOperator> AdditiveOperatorPrinter;

	@Inject
	public AdditiveExpressionPrinterImpl(Printer<AdditiveExpressionChild> additiveExpressionChildPrinter,
			Printer<AdditiveOperator> additiveOperatorPrinter) {
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
