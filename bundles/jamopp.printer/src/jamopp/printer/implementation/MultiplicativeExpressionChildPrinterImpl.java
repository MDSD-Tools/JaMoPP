package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MultiplicativeExpressionChild;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.UnaryExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MultiplicativeExpressionChildPrinterImpl implements Printer<MultiplicativeExpressionChild> {

	private final Printer<UnaryExpressionChild> unaryExpressionChildPrinter;
	private final Printer<UnaryExpression> unaryExpressionPrinter;

	@Inject
	public MultiplicativeExpressionChildPrinterImpl(Printer<UnaryExpression> unaryExpressionPrinter,
			Printer<UnaryExpressionChild> unaryExpressionChildPrinter) {
		this.unaryExpressionPrinter = unaryExpressionPrinter;
		this.unaryExpressionChildPrinter = unaryExpressionChildPrinter;
	}

	@Override
	public void print(MultiplicativeExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof UnaryExpression) {
			this.unaryExpressionPrinter.print((UnaryExpression) element, writer);
		} else {
			this.unaryExpressionChildPrinter.print((UnaryExpressionChild) element, writer);
		}
	}

}
