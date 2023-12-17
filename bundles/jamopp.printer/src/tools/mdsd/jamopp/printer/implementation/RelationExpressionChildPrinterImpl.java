package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import com.google.inject.Inject;

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