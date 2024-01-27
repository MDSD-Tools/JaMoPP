package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.model.java.operators.EqualityOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EqualityExpressionPrinterImpl implements Printer<EqualityExpression> {

	private final Printer<EqualityExpressionChild> equalityExpressionChildPrinter;
	private final Printer<EqualityOperator> equalityOperatorPrinter;

	@Inject
	public EqualityExpressionPrinterImpl(final Printer<EqualityExpressionChild> equalityExpressionChildPrinter,
			final Printer<EqualityOperator> equalityOperatorPrinter) {
		this.equalityExpressionChildPrinter = equalityExpressionChildPrinter;
		this.equalityOperatorPrinter = equalityOperatorPrinter;
	}

	@Override
	public void print(final EqualityExpression element, final BufferedWriter writer) throws IOException {
		equalityExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			equalityOperatorPrinter.print(element.getEqualityOperators().get(index - 1), writer);
			equalityExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
