package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class UnaryExpressionChildPrinterImpl implements Printer<UnaryExpressionChild> {

	private final Printer<PrefixUnaryModificationExpression> PrefixUnaryModificationExpressionPrinter;
	private final Printer<SuffixUnaryModificationExpression> SuffixUnaryModificationExpressionPrinter;
	private final Printer<UnaryModificationExpressionChild> UnaryModificationExpressionChildPrinter;

	@Inject
	public UnaryExpressionChildPrinterImpl(
			Printer<PrefixUnaryModificationExpression> prefixUnaryModificationExpressionPrinter,
			Printer<SuffixUnaryModificationExpression> suffixUnaryModificationExpressionPrinter,
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
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
