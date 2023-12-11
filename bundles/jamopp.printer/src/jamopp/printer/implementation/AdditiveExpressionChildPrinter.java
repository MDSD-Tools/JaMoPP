package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

import jamopp.printer.interfaces.Printer;

class AdditiveExpressionChildPrinter implements Printer<AdditiveExpressionChild> {

	private final MultiplicativeExpressionPrinter MultiplicativeExpressionPrinter;
	private final MultiplicativeExpressionChildPrinter MultiplicativeExpressionChildPrinter;
	
	public void print(AdditiveExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof MultiplicativeExpression) {
			MultiplicativeExpressionPrinter.print((MultiplicativeExpression) element, writer);
		} else {
			MultiplicativeExpressionChildPrinter.print((MultiplicativeExpressionChild) element, writer);
		}
	}

}
