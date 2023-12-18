package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpression;
import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EqualityExpressionChildPrinterImpl implements Printer<EqualityExpressionChild> {

	private final Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter;
	private final Printer<InstanceOfExpression> instanceOfExpressionPrinter;

	@Inject
	public EqualityExpressionChildPrinterImpl(Printer<InstanceOfExpression> instanceOfExpressionPrinter,
			Printer<InstanceOfExpressionChild> instanceOfExpressionChildPrinter) {
		this.instanceOfExpressionPrinter = instanceOfExpressionPrinter;
		this.instanceOfExpressionChildPrinter = instanceOfExpressionChildPrinter;
	}

	@Override
	public void print(EqualityExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof InstanceOfExpression) {
			this.instanceOfExpressionPrinter.print((InstanceOfExpression) element, writer);
		} else {
			this.instanceOfExpressionChildPrinter.print((InstanceOfExpressionChild) element, writer);
		}
	}

}
