package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.AndExpression;
import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.ExclusiveOrExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExclusiveOrExpressionChildPrinterImpl implements Printer<ExclusiveOrExpressionChild> {

	private final Printer<AndExpressionChild> andExpressionChildPrinter;
	private final Printer<AndExpression> andExpressionPrinter;

	@Inject
	public ExclusiveOrExpressionChildPrinterImpl(Printer<AndExpression> andExpressionPrinter,
			Printer<AndExpressionChild> andExpressionChildPrinter) {
		this.andExpressionPrinter = andExpressionPrinter;
		this.andExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(ExclusiveOrExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof AndExpression) {
			this.andExpressionPrinter.print((AndExpression) element, writer);
		} else {
			this.andExpressionChildPrinter.print((AndExpressionChild) element, writer);
		}
	}

}
