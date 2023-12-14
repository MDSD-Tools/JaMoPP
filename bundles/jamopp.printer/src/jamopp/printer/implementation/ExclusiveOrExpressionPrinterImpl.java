package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ExclusiveOrExpression;
import org.emftext.language.java.expressions.ExclusiveOrExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ExclusiveOrExpressionPrinterImpl implements Printer<ExclusiveOrExpression> {

	private final Printer<ExclusiveOrExpressionChild> ExclusiveOrExpressionChildPrinter;

	@Inject
	public ExclusiveOrExpressionPrinterImpl(Printer<ExclusiveOrExpressionChild> exclusiveOrExpressionChildPrinter) {
		ExclusiveOrExpressionChildPrinter = exclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(ExclusiveOrExpression element, BufferedWriter writer) throws IOException {
		ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" ^ ");
			ExclusiveOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}



}
