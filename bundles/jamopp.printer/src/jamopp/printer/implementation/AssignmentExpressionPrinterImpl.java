package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.operators.AssignmentOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
		this.assignmentExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getAssignmentOperator() != null) {
			this.assignmentOperatorPrinter.print(element.getAssignmentOperator(), writer);
			this.expressionPrinter.print(element.getValue(), writer);
		}
	}

}
