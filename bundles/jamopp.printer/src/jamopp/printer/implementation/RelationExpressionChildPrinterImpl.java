package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class RelationExpressionChildPrinterImpl implements Printer<RelationExpressionChild> {

	private final Printer<ShiftExpressionChild> ShiftExpressionChildPrinter;
	private final Printer<ShiftExpression> ShiftExpressionPrinter;

	@Inject
	public RelationExpressionChildPrinterImpl(Printer<ShiftExpression> shiftExpressionPrinter,
			Printer<ShiftExpressionChild> shiftExpressionChildPrinter) {
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