package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.PrefixUnaryModificationExpressionPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.UnaryModificationOperatorPrinterInt;

public class PrefixUnaryModificationExpressionPrinterImpl implements PrefixUnaryModificationExpressionPrinterInt {

	private final UnaryModificationOperatorPrinterInt UnaryModificationOperatorPrinter;
	private final UnaryModificationExpressionChildPrinterInt UnaryModificationExpressionChildPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinterImpl(
			UnaryModificationOperatorPrinterInt unaryModificationOperatorPrinter,
			UnaryModificationExpressionChildPrinterInt unaryModificationExpressionChildPrinter) {
		UnaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
		UnaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(PrefixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
