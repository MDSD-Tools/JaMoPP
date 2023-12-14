package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AssignmentExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AssignmentExpressionPrinterInt;
import jamopp.printer.interfaces.printer.AssignmentOperatorPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class AssignmentExpressionPrinterImpl implements AssignmentExpressionPrinterInt {

	private final AssignmentExpressionChildPrinterInt AssignmentExpressionChildPrinter;
	private final AssignmentOperatorPrinterInt AssignmentOperatorPrinter;
	private final ExpressionPrinterInt ExpressionPrinter;

	@Inject
	public AssignmentExpressionPrinterImpl(AssignmentExpressionChildPrinterInt assignmentExpressionChildPrinter,
			AssignmentOperatorPrinterInt assignmentOperatorPrinter, ExpressionPrinterInt expressionPrinter) {
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
