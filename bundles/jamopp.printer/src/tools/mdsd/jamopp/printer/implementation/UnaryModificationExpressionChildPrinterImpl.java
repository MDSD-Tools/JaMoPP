package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.CastExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpression;
import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.statements.Switch;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryModificationExpressionChildPrinterImpl implements Printer<UnaryModificationExpressionChild> {

	private final Printer<CastExpression> castExpressionPrinter;
	private final Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter;
	private final Printer<MethodReferenceExpression> methodReferenceExpressionPrinter;
	private final Printer<Switch> switchPrinter;

	@Inject
	public UnaryModificationExpressionChildPrinterImpl(Printer<Switch> switchPrinter,
			Printer<CastExpression> castExpressionPrinter,
			Printer<MethodReferenceExpression> methodReferenceExpressionPrinter,
			Printer<MethodReferenceExpressionChild> methodReferenceExpressionChildPrinter) {
		this.switchPrinter = switchPrinter;
		this.castExpressionPrinter = castExpressionPrinter;
		this.methodReferenceExpressionPrinter = methodReferenceExpressionPrinter;
		this.methodReferenceExpressionChildPrinter = methodReferenceExpressionChildPrinter;
	}

	@Override
	public void print(UnaryModificationExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof Switch) {
			this.switchPrinter.print((Switch) element, writer);
		} else if (element instanceof CastExpression) {
			this.castExpressionPrinter.print((CastExpression) element, writer);
		} else if (element instanceof MethodReferenceExpression) {
			this.methodReferenceExpressionPrinter.print((MethodReferenceExpression) element, writer);
		} else {
			this.methodReferenceExpressionChildPrinter.print((MethodReferenceExpressionChild) element, writer);
		}
	}

}
