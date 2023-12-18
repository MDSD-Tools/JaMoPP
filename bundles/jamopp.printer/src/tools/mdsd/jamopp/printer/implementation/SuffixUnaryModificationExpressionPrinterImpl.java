package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.model.java.operators.UnaryModificationOperator;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SuffixUnaryModificationExpressionPrinterImpl implements Printer<SuffixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter,
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter) {
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
	}

	@Override
	public void print(SuffixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		this.unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			this.unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
