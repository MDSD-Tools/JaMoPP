package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpression;
import tools.mdsd.jamopp.model.java.expressions.ShiftExpressionChild;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RelationExpressionChildPrinterImpl implements Printer<RelationExpressionChild> {

	private final Printer<ShiftExpressionChild> shiftExpressionChildPrinter;
	private final Printer<ShiftExpression> shiftExpressionPrinter;

	@Inject
	public RelationExpressionChildPrinterImpl(Printer<ShiftExpression> shiftExpressionPrinter,
			Printer<ShiftExpressionChild> shiftExpressionChildPrinter) {
		this.shiftExpressionPrinter = shiftExpressionPrinter;
		this.shiftExpressionChildPrinter = shiftExpressionChildPrinter;
	}

	@Override
	public void print(RelationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ShiftExpression s) {
			this.shiftExpressionPrinter.print(s, writer);
		} else {
			this.shiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}

}