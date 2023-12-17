package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalOrExpression;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionPrinterImpl implements Printer<ConditionalOrExpression> {

	private final Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter;

	@Inject
	public ConditionalOrExpressionPrinterImpl(
			Printer<ConditionalOrExpressionChild> conditionalOrExpressionChildPrinter) {
		this.conditionalOrExpressionChildPrinter = conditionalOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalOrExpression element, BufferedWriter writer) throws IOException {
		this.conditionalOrExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" || ");
			this.conditionalOrExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
