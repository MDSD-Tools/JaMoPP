package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;

import jamopp.printer.interfaces.Printer;

class ShiftExpressionPrinter implements Printer<ShiftExpression>{

	private final ShiftExpressionChildPrinter ShiftExpressionChildPrinter;
	private final ShiftOperatorPrinter ShiftOperatorPrinter;
	
	public void print(ShiftExpression element, BufferedWriter writer) throws IOException {
		ShiftExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			ShiftOperatorPrinter.print(element.getShiftOperators().get(index - 1), writer);
			ShiftExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
