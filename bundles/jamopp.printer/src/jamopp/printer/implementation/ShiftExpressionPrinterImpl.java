package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ShiftExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ShiftExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ShiftOperatorPrinterInt;

public class ShiftExpressionPrinterImpl implements ShiftExpressionPrinterInt {

	private final ShiftExpressionChildPrinterInt ShiftExpressionChildPrinter;
	private final ShiftOperatorPrinterInt ShiftOperatorPrinter;

	@Inject
	public ShiftExpressionPrinterImpl(ShiftExpressionChildPrinterInt shiftExpressionChildPrinter,
			ShiftOperatorPrinterInt shiftOperatorPrinter) {
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
