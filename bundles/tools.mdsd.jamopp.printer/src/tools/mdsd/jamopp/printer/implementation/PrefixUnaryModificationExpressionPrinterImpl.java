package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrefixUnaryModificationExpressionPrinterImpl implements Printer<PrefixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter,
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(PrefixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			this.unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		this.unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
