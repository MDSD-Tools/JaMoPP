package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AssignmentExpression;
import org.emftext.language.java.expressions.AssignmentExpressionChild;
import org.emftext.language.java.expressions.Expression;
import org.emftext.language.java.expressions.LambdaExpression;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.printer.AssignmentExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AssignmentExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExpressionPrinterInt;
import jamopp.printer.interfaces.printer.LambdaExpressionPrinterInt;

public class ExpressionPrinterImpl implements ExpressionPrinterInt {

	private final Provider<LambdaExpressionPrinterInt> LambdaExpressionPrinter;
	private final Provider<AssignmentExpressionPrinterInt> AssignmentExpressionPrinter;
	private final Provider<AssignmentExpressionChildPrinterInt> AssignmentExpressionChildPrinter;

	@Inject
	public ExpressionPrinterImpl(Provider<LambdaExpressionPrinterInt> lambdaExpressionPrinter,
			Provider<AssignmentExpressionPrinterInt> assignmentExpressionPrinter,
			Provider<AssignmentExpressionChildPrinterInt> assignmentExpressionChildPrinter) {
		LambdaExpressionPrinter = lambdaExpressionPrinter;
		AssignmentExpressionPrinter = assignmentExpressionPrinter;
		AssignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
	}

	@Override
	public void print(Expression element, BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			LambdaExpressionPrinter.get().print((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			AssignmentExpressionPrinter.get().print((AssignmentExpression) element, writer);
		} else {
			AssignmentExpressionChildPrinter.get().print((AssignmentExpressionChild) element, writer);
		}
	}

}
