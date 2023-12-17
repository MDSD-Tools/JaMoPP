package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpression;
import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.ConditionalOrExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalOrExpressionChildPrinterImpl implements Printer<ConditionalOrExpressionChild> {

	private final Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter;
	private final Printer<ConditionalAndExpression> conditionalAndExpressionPrinter;

	@Inject
	public ConditionalOrExpressionChildPrinterImpl(Printer<ConditionalAndExpression> conditionalAndExpressionPrinter,
			Printer<ConditionalAndExpressionChild> conditionalAndExpressionChildPrinter) {
		this.conditionalAndExpressionPrinter = conditionalAndExpressionPrinter;
		this.conditionalAndExpressionChildPrinter = conditionalAndExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof ConditionalAndExpression) {
			this.conditionalAndExpressionPrinter.print((ConditionalAndExpression) element, writer);
		} else {
			this.conditionalAndExpressionChildPrinter.print((ConditionalAndExpressionChild) element, writer);
		}
	}

}
