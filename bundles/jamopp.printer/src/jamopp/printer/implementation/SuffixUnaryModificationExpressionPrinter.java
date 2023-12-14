package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.SuffixUnaryModificationExpressionPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationOperatorPrinterInt;

public class SuffixUnaryModificationExpressionPrinter implements SuffixUnaryModificationExpressionPrinterInt {

	private final UnaryModificationExpressionChildPrinterInt UnaryModificationExpressionChildPrinter;
	private final UnaryModificationOperatorPrinterInt UnaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinter(
			UnaryModificationExpressionChildPrinterInt unaryModificationExpressionChildPrinter,
			UnaryModificationOperatorPrinterInt unaryModificationOperatorPrinter) {
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
