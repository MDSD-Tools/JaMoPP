package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionChildPrinterImpl implements Printer<MultiplicativeExpressionChild> {

	private final Printer<UnaryExpressionChild> UnaryExpressionChildPrinter;
	private final Printer<UnaryExpression> UnaryExpressionPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinterImpl(Printer<UnaryExpression> unaryExpressionPrinter,
			Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		UnaryExpressionPrinter = unaryExpressionPrinter;
		UnaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(MultiplicativeExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			UnaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			UnaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
