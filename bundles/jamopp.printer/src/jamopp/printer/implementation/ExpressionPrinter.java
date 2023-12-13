package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;

public class ExpressionPrinter implements ExpressionPrinterInt {

	private final LambdaExpressionPrinter LambdaExpressionPrinter;
	private final AssignmentExpressionPrinter AssignmentExpressionPrinter;
	private final AssignmentExpressionChildPrinter AssignmentExpressionChildPrinter;

	@Inject
	public ExpressionPrinter(jamopp.printer.implementation.LambdaExpressionPrinter lambdaExpressionPrinter,
			jamopp.printer.implementation.AssignmentExpressionPrinter assignmentExpressionPrinter,
			jamopp.printer.implementation.AssignmentExpressionChildPrinter assignmentExpressionChildPrinter) {
		super();
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
