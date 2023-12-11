package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.PrefixUnaryModificationExpression;

import jamopp.printer.interfaces.Printer;

class PrefixUnaryModificationExpressionPrinter implements Printer<PrefixUnaryModificationExpression>{

	public void print(PrefixUnaryModificationExpression element,
			BufferedWriter writer) throws IOException {
		if (element.getOperator() != null) {
			UnaryModificationOperatorPrinter.print(element.getOperator(), writer);
		}
		UnaryModificationExpressionChildPrinter.print(element.getChild(), writer);
	}

}
