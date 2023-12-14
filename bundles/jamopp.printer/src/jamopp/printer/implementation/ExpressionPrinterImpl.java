package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AssignmentExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AssignmentExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.LambdaExpressionPrinterInt;

public class ExpressionPrinterImpl implements ExpressionPrinterInt {

	private final LambdaExpressionPrinterInt LambdaExpressionPrinter;
	private final AssignmentExpressionPrinterInt AssignmentExpressionPrinter;
	private final AssignmentExpressionChildPrinterInt AssignmentExpressionChildPrinter;

	@Inject
	public ExpressionPrinterImpl(LambdaExpressionPrinterInt lambdaExpressionPrinter,
			AssignmentExpressionPrinterInt assignmentExpressionPrinter,
			AssignmentExpressionChildPrinterInt assignmentExpressionChildPrinter) {
		LambdaExpressionPrinter = lambdaExpressionPrinter;
		AssignmentExpressionPrinter = assignmentExpressionPrinter;
		AssignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
	}

	@Override
	public void print(Expression element, BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			LambdaExpressionPrinter.print((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			AssignmentExpressionPrinter.print((AssignmentExpression) element, writer);
		} else {
			AssignmentExpressionChildPrinter.print((AssignmentExpressionChild) element, writer);
		}
	}

}
