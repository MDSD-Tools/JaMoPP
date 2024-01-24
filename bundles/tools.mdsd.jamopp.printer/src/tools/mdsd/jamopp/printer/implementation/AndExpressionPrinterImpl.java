package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.AndExpression;
import tools.mdsd.jamopp.model.java.expressions.AndExpressionChild;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AndExpressionPrinterImpl implements Printer<AndExpression> {

	private final Printer<AndExpressionChild> andExpressionChildPrinter;

	@Inject
	public AndExpressionPrinterImpl(final Printer<AndExpressionChild> andExpressionChildPrinter) {
		this.andExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(final AndExpression element, final BufferedWriter writer) throws IOException {
		andExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			andExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
