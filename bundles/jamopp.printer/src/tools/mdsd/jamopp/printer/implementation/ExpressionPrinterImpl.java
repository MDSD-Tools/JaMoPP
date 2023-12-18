package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.AssignmentExpression;
import tools.mdsd.jamopp.model.java.expressions.AssignmentExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.Expression;
import tools.mdsd.jamopp.model.java.expressions.LambdaExpression;

import com.google.inject.Inject;
import com.google.inject.Provider;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExpressionPrinterImpl implements Printer<Expression> {

	private final Provider<Printer<AssignmentExpressionChild>> assignmentExpressionChildPrinter;
	private final Provider<Printer<AssignmentExpression>> assignmentExpressionPrinter;
	private final Provider<Printer<LambdaExpression>> lambdaExpressionPrinter;

	@Inject
	public ExpressionPrinterImpl(Provider<Printer<LambdaExpression>> lambdaExpressionPrinter,
			Provider<Printer<AssignmentExpression>> assignmentExpressionPrinter,
			Provider<Printer<AssignmentExpressionChild>> assignmentExpressionChildPrinter) {
		this.lambdaExpressionPrinter = lambdaExpressionPrinter;
		this.assignmentExpressionPrinter = assignmentExpressionPrinter;
		this.assignmentExpressionChildPrinter = assignmentExpressionChildPrinter;
	}

	@Override
	public void print(Expression element, BufferedWriter writer) throws IOException {
		if (element instanceof LambdaExpression) {
			this.lambdaExpressionPrinter.get().print((LambdaExpression) element, writer);
		} else if (element instanceof AssignmentExpression) {
			this.assignmentExpressionPrinter.get().print((AssignmentExpression) element, writer);
		} else {
			this.assignmentExpressionChildPrinter.get().print((AssignmentExpressionChild) element, writer);
		}
	}

}
