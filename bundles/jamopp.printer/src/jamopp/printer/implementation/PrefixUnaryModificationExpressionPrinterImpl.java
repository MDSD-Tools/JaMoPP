package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;
import org.emftext.language.java.expressions.UnaryModificationExpressionChild;
import org.emftext.language.java.operators.UnaryModificationOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class PrefixUnaryModificationExpressionPrinterImpl implements Printer<PrefixUnaryModificationExpression> {

	private final Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter;
	private final Printer<UnaryModificationOperator> unaryModificationOperatorPrinter;

	@Inject
	public PrefixUnaryModificationExpressionPrinterImpl(
			Printer<UnaryModificationOperator> unaryModificationOperatorPrinter,
			Printer<UnaryModificationExpressionChild> unaryModificationExpressionChildPrinter) {
		this.unaryModificationOperatorPrinter = unaryModificationOperatorPrinter;
		this.unaryModificationExpressionChildPrinter = unaryModificationExpressionChildPrinter;
	}

	@Override
	public void print(PrefixUnaryModificationExpression element, BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			this.unaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		this.unaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
