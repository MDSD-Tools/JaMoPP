package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ShiftExpressionPrinter implements IShiftExpressionPrinter {

	private final ShiftExpressionChildPrinter ShiftExpressionChildPrinter;
	private final ShiftOperatorPrinter ShiftOperatorPrinter;

	@Inject
	public ShiftExpressionPrinter(jamopp.printer.implementation.ShiftExpressionChildPrinter shiftExpressionChildPrinter,
			jamopp.printer.implementation.ShiftOperatorPrinter shiftOperatorPrinter) {
		super();
		ShiftExpressionChildPrinter = shiftExpressionChildPrinter;
		ShiftOperatorPrinter = shiftOperatorPrinter;
	}

	@Override
	public void print(ShiftExpression element, BufferedWriter writer) throws IOException {
		ShiftExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			ShiftOperatorPrinter.print(element.getShiftOperators().get(index - 1), writer);
			ShiftExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
