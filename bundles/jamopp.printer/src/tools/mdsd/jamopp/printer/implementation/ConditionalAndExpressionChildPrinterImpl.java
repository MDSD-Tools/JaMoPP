package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ConditionalAndExpressionChild;
import org.emftext.language.java.expressions.InclusiveOrExpression;
import org.emftext.language.java.expressions.InclusiveOrExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ConditionalAndExpressionChildPrinterImpl implements Printer<ConditionalAndExpressionChild> {

	private final Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter;
	private final Printer<InclusiveOrExpression> inclusiveOrExpressionPrinter;

	@Inject
	public ConditionalAndExpressionChildPrinterImpl(Printer<InclusiveOrExpression> inclusiveOrExpressionPrinter,
			Printer<InclusiveOrExpressionChild> inclusiveOrExpressionChildPrinter) {
		this.inclusiveOrExpressionPrinter = inclusiveOrExpressionPrinter;
		this.inclusiveOrExpressionChildPrinter = inclusiveOrExpressionChildPrinter;
	}

	@Override
	public void print(ConditionalAndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof InclusiveOrExpression) {
			this.inclusiveOrExpressionPrinter.print((InclusiveOrExpression) element, writer);
		} else {
			this.inclusiveOrExpressionChildPrinter.print((InclusiveOrExpressionChild) element, writer);
		}
	}

}
