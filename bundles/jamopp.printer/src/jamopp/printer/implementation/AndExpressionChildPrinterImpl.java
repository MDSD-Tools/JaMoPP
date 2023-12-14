package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AndExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.EqualityExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.EqualityExpressionPrinterInt;

public class AndExpressionChildPrinterImpl implements AndExpressionChildPrinterInt {

	private final EqualityExpressionPrinterInt EqualityExpressionPrinter;
	private final EqualityExpressionChildPrinterInt EqualityExpressionChildPrinter;

	@Inject
	public AndExpressionChildPrinterImpl(EqualityExpressionPrinterInt equalityExpressionPrinter,
			EqualityExpressionChildPrinterInt equalityExpressionChildPrinter) {

		EqualityExpressionPrinter = equalityExpressionPrinter;
		EqualityExpressionChildPrinter = equalityExpressionChildPrinter;
	}

	@Override
	public void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			EqualityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			EqualityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}
