package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.PrefixUnaryModificationExpressionPrinterInt;

public class PrefixUnaryModificationExpressionPrinter implements PrefixUnaryModificationExpressionPrinterInt {

	private final UnaryModificationOperatorPrinter UnaryModificationOperatorPrinter;
	private final UnaryModificationExpressionChildPrinter UnaryModificationExpressionChildPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinter(
			jamopp.printer.implementation.UnaryModificationOperatorPrinter unaryModificationOperatorPrinter,
			jamopp.printer.implementation.UnaryModificationExpressionChildPrinter unaryModificationExpressionChildPrinter) {
		super();
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
