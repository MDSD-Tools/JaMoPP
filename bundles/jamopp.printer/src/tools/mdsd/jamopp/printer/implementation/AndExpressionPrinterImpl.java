package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;
import org.emftext.language.java.expressions.AndExpressionChild;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AndExpressionPrinterImpl implements Printer<AndExpression> {

	private final Printer<AndExpressionChild> andExpressionChildPrinter;

	@Inject
	public AndExpressionPrinterImpl(Printer<AndExpressionChild> andExpressionChildPrinter) {
		this.andExpressionChildPrinter = andExpressionChildPrinter;
	}

	@Override
	public void print(AndExpression element, BufferedWriter writer) throws IOException {
		this.andExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			this.andExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
