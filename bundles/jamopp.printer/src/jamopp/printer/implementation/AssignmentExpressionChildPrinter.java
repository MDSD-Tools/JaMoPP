package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.ConditionalExpression;
import org.emftext.language.java.expressions.ConditionalExpressionChild;

import jamopp.printer.interfaces.Printer;

class AssignmentExpressionChildPrinter implements Printer<AssignmentExpressionChild>{

	public void print(AssignmentExpressionChild element, BufferedWriter writer)
			throws IOException {
		if (element instanceof ConditionalExpression) {
			ConditionalExpressionPrinter.print((ConditionalExpression) element, writer);
		} else {
			ConditionalExpressionChildPrinter.print((ConditionalExpressionChild) element, writer);
		}
	}

}
