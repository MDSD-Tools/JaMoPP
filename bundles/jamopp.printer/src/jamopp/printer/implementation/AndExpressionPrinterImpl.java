package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AndExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.AndExpressionPrinterInt;

public class AndExpressionPrinterImpl implements Printer<AndExpression> {

	private final Printer<AndExpressionChild> AndExpressionChildPrinter;

	@Inject
	public AndExpressionPrinterImpl(Printer<AndExpressionChild> andExpressionChildPrinter) {
		AndExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(AndExpression element, BufferedWriter writer) throws IOException {
		AndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			AndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
