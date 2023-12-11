package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;

import jamopp.printer.interfaces.Printer;

class AssignmentExpressionPrinter implements Printer<AssignmentExpression>{

	public void print(AssignmentExpression element, BufferedWriter writer)
			throws IOException {
		AssignmentExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			AssignmentOperatorPrinter.print(element.getAssignmentOperator(), writer);
			ExpressionPrinter.print(element.getValue(), writer);
		}
	}

}
