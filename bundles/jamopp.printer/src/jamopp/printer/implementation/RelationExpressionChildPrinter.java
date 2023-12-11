package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class RelationExpressionChildPrinter implements Printer<RelationExpressionChild> {

	private final ShiftExpressionPrinter ShiftExpressionPrinter;
	private final ShiftExpressionChildPrinter ShiftExpressionChildPrinter;

	@Inject
	public RelationExpressionChildPrinter(jamopp.printer.implementation.ShiftExpressionPrinter shiftExpressionPrinter,
			jamopp.printer.implementation.ShiftExpressionChildPrinter shiftExpressionChildPrinter) {
		super();
		ShiftExpressionPrinter = shiftExpressionPrinter;
		ShiftExpressionChildPrinter = shiftExpressionChildPrinter;
	}

	public void print(RelationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ShiftExpression) {
			ShiftExpressionPrinter.print((ShiftExpression) element, writer);
		} else {
			ShiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}

}
