package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.operators.UnaryModificationOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class PrefixUnaryModificationExpressionPrinterImpl implements Printer<PrefixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> UnaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> UnaryModificationOperatorPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter,
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
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
