package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AssignmentExpressionPrinterInt;

class AssignmentExpressionPrinter implements AssignmentExpressionPrinterInt {

	private final AssignmentExpressionChildPrinter AssignmentExpressionChildPrinter;
	private final AssignmentOperatorPrinter AssignmentOperatorPrinter;
	private final ExpressionPrinter ExpressionPrinter;

	@Inject
	public AssignmentExpressionPrinter(
			jamopp.printer.implementation.AssignmentExpressionChildPrinter assignmentExpressionChildPrinter,
			jamopp.printer.implementation.AssignmentOperatorPrinter assignmentOperatorPrinter,
			jamopp.printer.implementation.ExpressionPrinter expressionPrinter) {
		super();
		AssignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
		AssignmentOperatorPrinter = assignmentOperatorPrinter;
		ExpressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AssignmentExpression element, BufferedWriter writer) throws IOException {
		AssignmentExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			AssignmentOperatorPrinter.print(element.getAssignmentOperator(), writer);
			ExpressionPrinter.print(element.getValue(), writer);
		}
	}

}
