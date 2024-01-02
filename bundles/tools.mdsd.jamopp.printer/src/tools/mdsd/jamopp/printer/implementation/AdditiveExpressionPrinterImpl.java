package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.AdditiveExpression;
import tools.mdsd.jamopp.model.java.expressions.AdditiveExpressionChild;
import tools.mdsd.jamopp.model.java.operators.AdditiveOperator;

import javax.inject.Inject;

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
