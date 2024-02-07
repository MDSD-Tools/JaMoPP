package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;
import javax.inject.Provider;

import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.LambdaExpression;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExpressionPrinterImpl implements Printer<Expression> {

	private final Provider<Printer<AssignmentExpressionChild>> assignmentExpressionChildPrinter;
	private final Provider<Printer<AssignmentExpression>> assignmentExpressionPrinter;
	private final Provider<Printer<LambdaExpression>> lambdaExpressionPrinter;

	@Inject
	public ExpressionPrinterImpl(final Provider<Printer<LambdaExpression>> lambdaExpressionPrinter,
			final Provider<Printer<AssignmentExpression>> assignmentExpressionPrinter,
			final Provider<Printer<AssignmentExpressionChild>> assignmentExpressionChildPrinter) {
		this.lambdaExpressionPrinter = lambdaExpressionPrinter;
		this.assignmentExpressionPrinter = assignmentExpressionPrinter;
		this.assignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
	}

	@Override
	public void print(final Expression element, final BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			lambdaExpressionPrinter.get().print((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			assignmentExpressionPrinter.get().print((AssignmentExpression) element, writer);
		} else {
			assignmentExpressionChildPrinter.get().print((AssignmentExpressionChild) element, writer);
		}
	}

}
