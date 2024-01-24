package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AndExpression;
import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExclusiveOrExpressionChildPrinterImpl implements Printer<ExclusiveOrExpressionChild> {

	private final Printer<AndExpressionChild> andExpressionChildPrinter;
	private final Printer<AndExpression> andExpressionPrinter;

	@Inject
	public ExclusiveOrExpressionChildPrinterImpl(final Printer<AndExpression> andExpressionPrinter,
			final Printer<AndExpressionChild> andExpressionChildPrinter) {
		this.andExpressionPrinter = andExpressionPrinter;
		this.andExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(final ExclusiveOrExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof AndExpression) {
			andExpressionPrinter.print((AndExpression) element, writer);
		} else {
			andExpressionChildPrinter.print((AndExpressionChild) element, writer);
		}
	}

}
