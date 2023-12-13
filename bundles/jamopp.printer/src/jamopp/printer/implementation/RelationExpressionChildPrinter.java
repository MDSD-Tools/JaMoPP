package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.RelationExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ShiftExpressionPrinterInt;

public class RelationExpressionChildPrinter implements RelationExpressionChildPrinterInt {

	private final ShiftExpressionPrinterInt ShiftExpressionPrinter;
	private final ShiftExpressionChildPrinter ShiftExpressionChildPrinter;

	@Inject
	public RelationExpressionChildPrinter(ShiftExpressionPrinterInt shiftExpressionPrinter,
			jamopp.printer.implementation.ShiftExpressionChildPrinter shiftExpressionChildPrinter) {
		super();
		ShiftExpressionPrinter = shiftExpressionPrinter;
		ShiftExpressionChildPrinter = shiftExpressionChildPrinter;
	}

	@Override
	public void print(RelationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ShiftExpression s) {
			ShiftExpressionPrinter.print(s, writer);
		} else {
			ShiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}
}