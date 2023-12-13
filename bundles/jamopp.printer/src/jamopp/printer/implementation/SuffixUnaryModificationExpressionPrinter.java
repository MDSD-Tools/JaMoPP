package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.SuffixUnaryModificationExpressionPrinterInt;

class SuffixUnaryModificationExpressionPrinter implements SuffixUnaryModificationExpressionPrinterInt {

	private final UnaryModificationExpressionChildPrinter UnaryModificationExpressionChildPrinter;
	private final UnaryModificationOperatorPrinter UnaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinter(
			jamopp.printer.implementation.UnaryModificationExpressionChildPrinter unaryModificationExpressionChildPrinter,
			jamopp.printer.implementation.UnaryModificationOperatorPrinter unaryModificationOperatorPrinter) {
		super();
		UnaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
		UnaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
	}

	@Override
	public void print(SuffixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
