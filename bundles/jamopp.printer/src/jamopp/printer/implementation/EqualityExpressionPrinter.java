package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpression;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class EqualityExpressionPrinter implements Printer<EqualityExpression> {

	private final EqualityExpressionChildPrinter EqualityExpressionChildPrinter;
	private final EqualityOperatorPrinter EqualityOperatorPrinter;

	@Inject
	public EqualityExpressionPrinter(
			jamopp.printer.implementation.EqualityExpressionChildPrinter equalityExpressionChildPrinter,
			jamopp.printer.implementation.EqualityOperatorPrinter equalityOperatorPrinter) {
		super();
		EqualityExpressionChildPrinter = equalityExpressionChildPrinter;
		EqualityOperatorPrinter = equalityOperatorPrinter;
	}

	public void print(EqualityExpression element, BufferedWriter writer) throws IOException {
		EqualityExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (int index = 1; index < element.getChildren().size(); index++) {
			EqualityOperatorPrinter.print(element.getEqualityOperators().get(index - 1), writer);
			EqualityExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
