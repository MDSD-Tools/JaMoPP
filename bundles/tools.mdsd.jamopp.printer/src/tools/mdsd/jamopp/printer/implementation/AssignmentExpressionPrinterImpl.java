package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.operators.AssignmentOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AssignmentExpressionPrinterImpl implements Printer<AssignmentExpression> {

	private final Printer<AssignmentExpressionChild> assignmentExpressionChildPrinter;
	private final Printer<AssignmentOperator> assignmentOperatorPrinter;
	private final Printer<Expression> expressionPrinter;

	@Inject
	public AssignmentExpressionPrinterImpl(Printer<AssignmentExpressionChild> assignmentExpressionChildPrinter,
			Printer<AssignmentOperator> assignmentOperatorPrinter, Printer<Expression> expressionPrinter) {
		this.assignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
		this.assignmentOperatorPrinter = assignmentOperatorPrinter;
		this.expressionPrinter = expressionPrinter;
	}

	@Override
	public void print(AssignmentExpression element, BufferedWriter writer) throws IOException {
		assignmentExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			assignmentOperatorPrinter.print(element.getAssignmentOperator(), writer);
			expressionPrinter.print(element.getValue(), writer);
		}
	}

}
