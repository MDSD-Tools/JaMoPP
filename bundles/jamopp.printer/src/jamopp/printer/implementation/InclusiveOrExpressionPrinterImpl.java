package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InclusiveOrExpressionPrinterImpl implements Printer<InclusiveOrExpression> {

	private final Printer<InclusiveOrExpressionChild> InclusiveOrExpressionChildPrinter;

	@Inject
	public InclusiveOrExpressionPrinterImpl(Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter) {
		InclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(InclusiveOrExpression element, BufferedWriter writer) throws IOException {
		InclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" | ");
			InclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
