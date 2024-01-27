package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.PrefixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.SuffixUnaryModificationExpression;
import tools.mdsd.jamopp.model.java.expressions.UnaryExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.UnaryModificationExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UnaryExpressionChildPrinterImpl implements Printer<UnaryExpressionChild> {

	private final Printer<PrefixUnaryModificationExpression> prefixUnaryModificationExpressionPrinter;
	private final Printer<SuffixUnaryModificationExpression> suffixUnaryModificationExpressionPrinter;
	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;

	@Inject
	public UnaryExpressionChildPrinterImpl(
			final Printer<PrefixUnaryModificationExpression> prefixUnaryModificationExpressionPrinter,
			final Printer<SuffixUnaryModificationExpression> suffixUnaryModificationExpressionPrinter,
			final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
		this.prefixUnaryModificationExpressionPrinter = prefixUnaryModificationExpressionPrinter;
		this.suffixUnaryModificationExpressionPrinter = suffixUnaryModificationExpressionPrinter;
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(final UnaryExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof PrefixUnaryModificationExpression) {
			prefixUnaryModificationExpressionPrinter.print((PrefixUnaryModificationExpression) element, writer);
		} else if (element instanceof SuffixUnaryModificationExpression) {
			suffixUnaryModificationExpressionPrinter.print((SuffixUnaryModificationExpression) element, writer);
		} else {
			unaryModificationExpressionChildPrinter.print((UnaryModificationExpressionChild) element, writer);
		}
	}

}
