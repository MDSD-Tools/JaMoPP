package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.InstanceOfExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.RelationExpression;
import tools.mdsd.jamopp.model.java.expressions.RelationExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class InstanceOfExpressionChildPrinterImpl implements Printer<InstanceOfExpressionChild> {

	private final Printer<RelationExpressionChild> relationExpressionChildPrinter;
	private final Printer<RelationExpression> relationExpressionPrinter;

	@Inject
	public InstanceOfExpressionChildPrinterImpl(final Printer<RelationExpression> relationExpressionPrinter,
			final Printer<RelationExpressionChild> relationExpressionChildPrinter) {
		this.relationExpressionPrinter = relationExpressionPrinter;
		this.relationExpressionChildPrinter = relationExpressionChildPrinter;
	}

	@Override
	public void print(final InstanceOfExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof RelationExpression) {
			relationExpressionPrinter.print((RelationExpression) element, writer);
		} else {
			relationExpressionChildPrinter.print((RelationExpressionChild) element, writer);
		}
	}

}
