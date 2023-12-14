package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.operators.UnaryModificationOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SuffixUnaryModificationExpressionPrinterImpl implements Printer<SuffixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> UnaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> UnaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter,
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter) {
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
