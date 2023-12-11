package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.SuffixUnaryModificationExpression;

import jamopp.printer.interfaces.Printer;

class SuffixUnaryModificationExpressionPrinter implements Printer<SuffixUnaryModificationExpression>{

	private final UnaryModificationExpressionChildPrinter UnaryModificationExpressionChildPrinter;
	private final UnaryModificationOperatorPrinter UnaryModificationOperatorPrinter;
	
	public void print(SuffixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
	}

}
