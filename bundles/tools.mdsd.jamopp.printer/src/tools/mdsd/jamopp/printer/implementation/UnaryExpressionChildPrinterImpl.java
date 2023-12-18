package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryExpressionChildPrinterImpl implements Printer<UnaryExpressionChild> {

	private final Printer<PrefixUnaryModificationExpression> prefixUnaryModificationExpressionPrinter;
	private final Printer<SuffixUnaryModificationExpression> suffixUnaryModificationExpressionPrinter;
	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;

	@Inject
	public UnaryExpressionChildPrinterImpl(
			Printer<PrefixUnaryModificationExpression> prefixUnaryModificationExpressionPrinter,
			Printer<SuffixUnaryModificationExpression> suffixUnaryModificationExpressionPrinter,
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
		this.prefixUnaryModificationExpressionPrinter = prefixUnaryModificationExpressionPrinter;
		this.suffixUnaryModificationExpressionPrinter = suffixUnaryModificationExpressionPrinter;
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(UnaryExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof PrefixUnaryModificationExpression) {
			this.prefixUnaryModificationExpressionPrinter.print((PrefixUnaryModificationExpression) element, writer);
		} else if (element instanceof SuffixUnaryModificationExpression) {
			this.suffixUnaryModificationExpressionPrinter.print((SuffixUnaryModificationExpression) element, writer);
		} else {
			this.unaryModificationExpressionChildPrinter.print((UnaryModificationExpressionChild) element, writer);
		}
	}

}
