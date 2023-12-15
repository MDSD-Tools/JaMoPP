package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.AndExpressionChild;
import org.emftext.language.java.expressions.EqualityExpression;
import org.emftext.language.java.expressions.EqualityExpressionChild;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AndExpressionChildPrinterImpl implements Printer<AndExpressionChild> {

	private final Printer<EqualityExpressionChild> equalityExpressionChildPrinter;
	private final Printer<EqualityExpression> equalityExpressionPrinter;

	@Inject
	public AndExpressionChildPrinterImpl(Printer<EqualityExpression> equalityExpressionPrinter,
			Printer<EqualityExpressionChild> equalityExpressionChildPrinter) {

		this.equalityExpressionPrinter = equalityExpressionPrinter;
		this.equalityExpressionChildPrinter = equalityExpressionChildPrinter;
	}

	@Override
	public void print(AndExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof EqualityExpression) {
			this.equalityExpressionPrinter.print((EqualityExpression) element, writer);
		} else {
			this.equalityExpressionChildPrinter.print((EqualityExpressionChild) element, writer);
		}
	}

}