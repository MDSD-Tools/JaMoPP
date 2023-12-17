package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpression;
import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.operators.AdditiveOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AdditiveExpressionPrinterImpl implements Printer<AdditiveExpression> {

	private final Printer<AdditiveExpressionChild> additiveExpressionChildPrinter;
	private final Printer<AdditiveOperator> additiveOperatorPrinter;

	@Inject
	public AdditiveExpressionPrinterImpl(Printer<AdditiveExpressionChild> additiveExpressionChildPrinter,
			Printer<AdditiveOperator> additiveOperatorPrinter) {
		this.additiveExpressionChildPrinter = additiveExpressionChildPrinter;
		this.additiveOperatorPrinter = additiveOperatorPrinter;
	}

	@Override
	public void print(AdditiveExpression element, BufferedWriter writer) throws IOException {
		this.additiveExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			this.additiveOperatorPrinter.print(element.getAdditiveOperators().get(index - 1), writer);
			this.additiveExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
