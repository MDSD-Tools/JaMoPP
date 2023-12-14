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

	private final Printer<AssignmentExpressionChild> AssignmentExpressionChildPrinter;
	private final Printer<AssignmentOperator> AssignmentOperatorPrinter;
	private final Printer<Expression> ExpressionPrinter;

	@Inject
	public AssignmentExpressionPrinterImpl(Printer<AssignmentExpressionChild> assignmentExpressionChildPrinter,
			Printer<AssignmentOperator> assignmentOperatorPrinter, Printer<Expression> expressionPrinter) {
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
