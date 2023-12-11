package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AndExpressionChildPrinter implements Printer<AndExpressionChild> {

	private final EqualityExpressionPrinter EqualityExpressionPrinter;
	private final EqualityExpressionChildPrinter EqualityExpressionChildPrinter;

	@Inject
	public AndExpressionChildPrinter(jamopp.printer.implementation.EqualityExpressionPrinter equalityExpressionPrinter,
			jamopp.printer.implementation.EqualityExpressionChildPrinter equalityExpressionChildPrinter) {
		super();
		EqualityExpressionPrinter = equalityExpressionPrinter;
		EqualityExpressionChildPrinter = equalityExpressionChildPrinter;
	}

	public void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			EqualityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			EqualityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}
