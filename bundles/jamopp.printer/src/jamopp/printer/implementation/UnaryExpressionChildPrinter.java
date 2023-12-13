package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.UnaryExpressionChildPrinterInt;

class UnaryExpressionChildPrinter implements Printer<UnaryExpressionChild>, UnaryExpressionChildPrinterInt {

	private final PrefixUnaryModificationExpressionPrinter PrefixUnaryModificationExpressionPrinter;
	private final SuffixUnaryModificationExpressionPrinter SuffixUnaryModificationExpressionPrinter;
	private final UnaryModificationExpressionChildPrinter UnaryModificationExpressionChildPrinter;

	@Inject
	public UnaryExpressionChildPrinter(
			PrefixUnaryModificationExpressionPrinter prefixUnaryModificationExpressionPrinter,
			jamopp.printer.implementation.SuffixUnaryModificationExpressionPrinter suffixUnaryModificationExpressionPrinter,
			jamopp.printer.implementation.UnaryModificationExpressionChildPrinter unaryModificationExpressionChildPrinter) {
		super();
		PrefixUnaryModificationExpressionPrinter = prefixUnaryModificationExpressionPrinter;
		SuffixUnaryModificationExpressionPrinter = suffixUnaryModificationExpressionPrinter;
		UnaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(UnaryExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof PrefixUnaryModificationExpression) {
			PrefixUnaryModificationExpressionPrinter.print((PrefixUnaryModificationExpression) element, writer);
		} else if (element instanceof SuffixUnaryModificationExpression) {
			SuffixUnaryModificationExpressionPrinter.print((SuffixUnaryModificationExpression) element, writer);
		} else {
			UnaryModificationExpressionChildPrinter.print((UnaryModificationExpressionChild) element, writer);
		}
	}

}
