package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.PrefixUnaryModificationExpressionPrinterInt;
import jamopp.printer.interfaces.printer.SuffixUnaryModificationExpressionPrinterInt;
import jamopp.printer.interfaces.printer.UnaryExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationExpressionChildPrinterInt;

public class UnaryExpressionChildPrinter implements UnaryExpressionChildPrinterInt {

	private final PrefixUnaryModificationExpressionPrinterInt PrefixUnaryModificationExpressionPrinter;
	private final SuffixUnaryModificationExpressionPrinterInt SuffixUnaryModificationExpressionPrinter;
	private final UnaryModificationExpressionChildPrinterInt UnaryModificationExpressionChildPrinter;

	@Inject
	public UnaryExpressionChildPrinter(
			PrefixUnaryModificationExpressionPrinterInt prefixUnaryModificationExpressionPrinter,
			SuffixUnaryModificationExpressionPrinterInt suffixUnaryModificationExpressionPrinter,
			UnaryModificationExpressionChildPrinterInt unaryModificationExpressionChildPrinter) {
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
