package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrefixUnaryModificationExpressionPrinterImpl implements Printer<PrefixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinterImpl(
			final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter,
			final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(final PrefixUnaryModificationExpression element, final BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
