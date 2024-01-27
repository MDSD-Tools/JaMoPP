package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.statements.Switch;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryModificationExpressionChildPrinterImpl implements Printer<UnaryModificationExpressionChild> {

	private final Printer<CastExpression> castExpressionPrinter;
	private final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter;
	private final Printer<MethodReferenceExpression> methodReferenceExpressionPrinter;
	private final Printer<Switch> switchPrinter;

	@Inject
	public UnaryModificationExpressionChildPrinterImpl(final Printer<Switch> switchPrinter,
			final Printer<CastExpression> castExpressionPrinter,
			final Printer<MethodReferenceExpression> methodReferenceExpressionPrinter,
			final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter) {
		this.switchPrinter = switchPrinter;
		this.castExpressionPrinter = castExpressionPrinter;
		this.methodReferenceExpressionPrinter = methodReferenceExpressionPrinter;
		this.methodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
	}

	@Override
	public void print(final UnaryModificationExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			switchPrinter.print((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			castExpressionPrinter.print((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			methodReferenceExpressionPrinter.print((MethodReferenceExpression) element, writer);
		} else {
			methodReferenceExpressionChildPrinter.print((MethodReferenceExpressionChild) element, writer);
		}
	}

}
