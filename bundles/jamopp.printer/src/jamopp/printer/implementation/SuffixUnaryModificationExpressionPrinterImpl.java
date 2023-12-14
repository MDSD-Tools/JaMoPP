package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.operators.UnaryModificationOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SuffixUnaryModificationExpressionPrinterImpl implements Printer<SuffixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public SuffixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter,
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter) {
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
	}

	@Override
	public void print(SuffixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		this.unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			this.unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
