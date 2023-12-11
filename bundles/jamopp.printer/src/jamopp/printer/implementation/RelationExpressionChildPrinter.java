package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.RelationExpressionChild;
import org.emftext.language.java.expressions.ShiftExpression;
import org.emftext.language.java.expressions.ShiftExpressionChild;

import jamopp.printer.interfaces.Printer;

class RelationExpressionChildPrinter implements Printer<RelationExpressionChild>{

	public void print(RelationExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ShiftExpression) {
			ShiftExpressionPrinter.print((ShiftExpression) element, writer);
		} else {
			ShiftExpressionChildPrinter.print((ShiftExpressionChild) element, writer);
		}
	}

}
