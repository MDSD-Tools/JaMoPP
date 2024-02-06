package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;
import tools.mdsd.jamopp.model.java.operators.ShiftOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ShiftExpressionPrinterImpl implements Printer<ShiftExpression> {

	private final Printer<ShiftExpressionChild> shiftExpressionChildPrinter;
	private final Printer<ShiftOperator> shiftOperatorPrinter;

	@Inject
	public ShiftExpressionPrinterImpl(final Printer<ShiftExpressionChild> shiftExpressionChildPrinter,
			final Printer<ShiftOperator> shiftOperatorPrinter) {
		this.shiftExpressionChildPrinter = shiftExpressionChildPrinter;
		this.shiftOperatorPrinter = shiftOperatorPrinter;
	}

	@Override
	public void print(final ShiftExpression element, final BufferedWriter writer) throws IOException {
		shiftExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			shiftOperatorPrinter.print(element.getShiftOperators().get(index - 1), writer);
			shiftExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
