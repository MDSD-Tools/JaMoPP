package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AndExpressionPrinter implements Printer<AndExpression> {

	private final AndExpressionChildPrinter AndExpressionChildPrinter;

	@Inject
	public AndExpressionPrinter(jamopp.printer.implementation.AndExpressionChildPrinter andExpressionChildPrinter) {
		super();
		AndExpressionChildPrinter = andExpressionChildPrinter;
	}

	public void print(AndExpression element, BufferedWriter writer) throws IOException {
		AndExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			writer.append(" & ");
			AndExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
