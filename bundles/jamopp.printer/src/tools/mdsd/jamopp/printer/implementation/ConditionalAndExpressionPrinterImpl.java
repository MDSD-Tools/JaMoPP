package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpression;
import tools.mdsd.jamopp.model.java.expressions.ConditionalAndExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalAndExpressionPrinterImpl implements Printer<ConditionalAndExpression> {

	private final Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter;

	@Inject
	public ConditionalAndExpressionPrinterImpl(
			Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
		this.conditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalAndExpression element, BufferedWriter writer) throws IOException {
		this.conditionalAndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" && ");
			this.conditionalAndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
