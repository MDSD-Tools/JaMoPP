package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AdditiveExpressionChild;
import org.emftext.language.java.expressions.MultiplicativeExpression;
import org.emftext.language.java.expressions.MultiplicativeExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AdditiveExpressionChildPrinterImpl implements Printer<AdditiveExpressionChild> {

	private final Printer<MultiplicativeExpressionChild> MultiplicativeExpressionChildPrinter;
	private final Printer<MultiplicativeExpression> MultiplicativeExpressionPrinter;

	@Inject
	public AdditiveExpressionChildPrinterImpl(Printer<MultiplicativeExpression> multiplicativeExpressionPrinter,
			Printer<MultiplicativeExpressionChild> multiplicativeExpressionChildPrinter) {
		MultiplicativeExpressionPrinter = multiplicativeExpressionPrinter;
		MultiplicativeExpressionChildPrinter = multiplicativeExpressionChildPrinter;
	}

	@Override
	public void print(AdditiveExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof MultiplicativeExpression) {
			MultiplicativeExpressionPrinter.print((MultiplicativeExpression) element, writer);
		} else {
			MultiplicativeExpressionChildPrinter.print((MultiplicativeExpressionChild) element, writer);
		}
	}

}
