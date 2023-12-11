package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

import jamopp.printer.interfaces.Printer;

class ExclusiveOrExpressionChildPrinter implements Printer<ExclusiveOrExpressionChild> {

	private final AndExpressionPrinter AndExpressionPrinter;
	private final AndExpressionChildPrinter AndExpressionChildPrinter;

	public void print(ExclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof AndExpression) {
			AndExpressionPrinter.print((AndExpression) element, writer);
		} else {
			AndExpressionChildPrinter.print((AndExpressionChild) element, writer);
		}
	}

}
