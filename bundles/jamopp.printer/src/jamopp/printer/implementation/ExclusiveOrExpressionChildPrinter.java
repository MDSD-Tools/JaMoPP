package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AndExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AndExpressionPrinterInt;
import jamopp.printer.interfaces.printer.ExclusiveOrExpressionChildPrinterInt;

public class ExclusiveOrExpressionChildPrinter implements ExclusiveOrExpressionChildPrinterInt {

	private final AndExpressionPrinterInt AndExpressionPrinter;
	private final AndExpressionChildPrinterInt AndExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionChildPrinter(AndExpressionPrinterInt andExpressionPrinter,
			AndExpressionChildPrinterInt andExpressionChildPrinter) {
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
