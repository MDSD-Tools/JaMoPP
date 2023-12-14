package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;
import org.emftext.language.java.operators.EqualityOperator;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class EqualityExpressionPrinterImpl implements Printer<EqualityExpression> {

	private final Printer<EqualityExpressionChild> equalityExpressionChildPrinter;
	private final Printer<EqualityOperator> equalityOperatorPrinter;

	@Inject
	public EqualityExpressionPrinterImpl(Printer<EqualityExpressionChild> equalityExpressionChildPrinter,
			Printer<EqualityOperator> equalityOperatorPrinter) {
		this.equalityExpressionChildPrinter = equalityExpressionChildPrinter;
		this.equalityOperatorPrinter = equalityOperatorPrinter;
	}

	@Override
	public void print(EqualityExpression element, BufferedWriter writer) throws IOException {
		this.equalityExpressionChildPrinter.print(element.getChildren().get(0), writer);
		for (var index = 1; index < element.getChildren().size(); index++) {
			this.equalityOperatorPrinter.print(element.getEqualityOperators().get(index - 1), writer);
			this.equalityExpressionChildPrinter.print(element.getChildren().get(index), writer);
		}
	}

}
