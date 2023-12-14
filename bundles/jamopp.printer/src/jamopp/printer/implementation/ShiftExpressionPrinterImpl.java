package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;
import org.emftext.language.java.operators.ShiftOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ShiftExpressionPrinterImpl implements Printer<ShiftExpression> {

	private final Printer<ShiftExpressionChild> shiftExpressionChildPrinter;
	private final Printer<ShiftOperator> shiftOperatorPrinter;

	@Inject
	public ShiftExpressionPrinterImpl(Printer<ShiftExpressionChild> shiftExpressionChildPrinter,
			Printer<ShiftOperator> shiftOperatorPrinter) {
		this.shiftExpressionChildPrinter = shiftExpressionChildPrinter;
		this.shiftOperatorPrinter = shiftOperatorPrinter;
	}

	@Override
	public void print(ShiftExpression element, BufferedWriter writer) throws IOException {
		this.shiftExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			this.shiftOperatorPrinter.print(element.getShiftOperators().get(index - 1), writer);
			this.shiftExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
