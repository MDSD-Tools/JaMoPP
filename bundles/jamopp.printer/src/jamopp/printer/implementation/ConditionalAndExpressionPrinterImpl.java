package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ConditionalAndExpressionPrinterImpl implements Printer<ConditionalAndExpression> {

	private final Printer<ConditionalAndExpressionChild> ConditionalAndExpressionChildPrinter;

	@Inject
	public ConditionalAndExpressionPrinterImpl(
			Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
		ConditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalAndExpression element, BufferedWriter writer) throws IOException {
		ConditionalAndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			ConditionalAndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
