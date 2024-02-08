package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SuffixUnaryModificationExpressionPrinterImpl implements Printer<SuffixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinterImpl(
			final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter,
			final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter) {
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
	}

	@Override
	public void print(final SuffixUnaryModificationExpression element, final BufferedWriter writer) throws IOException {
		unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
