package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ExclusiveOrExpressionChildPrinter implements Printer<ExclusiveOrExpressionChild> {

	private final AndExpressionPrinter AndExpressionPrinter;
	private final AndExpressionChildPrinter AndExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionChildPrinter(jamopp.printer.implementation.AndExpressionPrinter andExpressionPrinter,
			jamopp.printer.implementation.AndExpressionChildPrinter andExpressionChildPrinter) {
		super();
		AndExpressionPrinter = andExpressionPrinter;
		AndExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(ExclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof AndExpression) {
			AndExpressionPrinter.print((AndExpression) element, writer);
		} else {
			AndExpressionChildPrinter.print((AndExpressionChild) element, writer);
		}
	}

}
