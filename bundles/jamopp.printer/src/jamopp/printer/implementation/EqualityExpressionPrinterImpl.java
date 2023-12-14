package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.operators.EqualityOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class EqualityExpressionPrinterImpl implements Printer<EqualityExpression> {

	private final Printer<EqualityExpressionChild> EqualityExpressionChildPrinter;
	private final Printer<EqualityOperator> EqualityOperatorPrinter;

	@Inject
	public EqualityExpressionPrinterImpl(Printer<EqualityExpressionChild> equalityExpressionChildPrinter,
			Printer<EqualityOperator> equalityOperatorPrinter) {
		EqualityExpressionChildPrinter = equalityExpressionChildPrinter;
		EqualityOperatorPrinter = equalityOperatorPrinter;
	}

	@Override
	public void print(EqualityExpression element, BufferedWriter writer) throws IOException {
		EqualityExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			EqualityOperatorPrinter.print(element.getEqualityOperators().get(index - 1), writer);
			EqualityExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
