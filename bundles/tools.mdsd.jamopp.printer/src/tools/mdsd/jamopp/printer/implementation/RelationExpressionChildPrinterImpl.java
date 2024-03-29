package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RelationExpressionChildPrinterImpl implements Printer<RelationExpressionChild> {

	private final Printer<ShiftExpressionChild> shiftExpressionChildPrinter;
	private final Printer<ShiftExpression> shiftExpressionPrinter;

	@Inject
	public RelationExpressionChildPrinterImpl(final Printer<ShiftExpression> shiftExpressionPrinter,
			final Printer<ShiftExpressionChild> shiftExpressionChildPrinter) {
		this.shiftExpressionPrinter = shiftExpressionPrinter;
		this.shiftExpressionChildPrinter = shiftExpressionChildPrinter;
	}

	@Override
	public void print(final RelationExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof ShiftExpression) {
			shiftExpressionPrinter.print((ShiftExpression) element, writer);
		} else {
			shiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}

}