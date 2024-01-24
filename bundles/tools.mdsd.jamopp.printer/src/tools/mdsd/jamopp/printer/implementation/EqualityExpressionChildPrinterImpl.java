package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EqualityExpressionChildPrinterImpl implements Printer<EqualityExpressionChild> {

	private final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter;
	private final Printer<InstanceOfExpression> instanceOfExpressionPrinter;

	@Inject
	public EqualityExpressionChildPrinterImpl(final Printer<InstanceOfExpression> instanceOfExpressionPrinter,
			final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter) {
		this.instanceOfExpressionPrinter = instanceOfExpressionPrinter;
		this.instanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
	}

	@Override
	public void print(final EqualityExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof InstanceOfExpression) {
			instanceOfExpressionPrinter.print((InstanceOfExpression) element, writer);
		} else {
			instanceOfExpressionChildPrinter.print((InstanceOfExpressionChild) element, writer);
		}
	}

}
