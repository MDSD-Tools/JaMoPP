package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AndExpressionChildPrinterImpl implements Printer<AndExpressionChild> {

	private final Printer<EqualityExpressionChild> equalityExpressionChildPrinter;
	private final Printer<EqualityExpression> equalityExpressionPrinter;

	@Inject
	public AndExpressionChildPrinterImpl(Printer<EqualityExpression> equalityExpressionPrinter,
			Printer<EqualityExpressionChild> equalityExpressionChildPrinter) {

		this.equalityExpressionPrinter = equalityExpressionPrinter;
		this.equalityExpressionChildPrinter = equalityExpressionChildPrinter;
	}

	@Override
	public void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			this.equalityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			this.equalityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}
