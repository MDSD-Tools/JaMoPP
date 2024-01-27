package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpression;
import tools.mdsd.jamopp.model.java.expressions.EqualityExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AndExpressionChildPrinterImpl implements Printer<AndExpressionChild> {

	private final Printer<EqualityExpressionChild> equalityExpressionChildPrinter;
	private final Printer<EqualityExpression> equalityExpressionPrinter;

	@Inject
	public AndExpressionChildPrinterImpl(final Printer<EqualityExpression> equalityExpressionPrinter,
			final Printer<EqualityExpressionChild> equalityExpressionChildPrinter) {

		this.equalityExpressionPrinter = equalityExpressionPrinter;
		this.equalityExpressionChildPrinter = equalityExpressionChildPrinter;
	}

	@Override
	public void print(final AndExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			equalityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			equalityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}
