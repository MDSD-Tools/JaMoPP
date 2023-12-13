package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionChildPrinterInt;

public class MethodReferenceExpressionChildPrinter implements MethodReferenceExpressionChildPrinterInt {

	private final LiteralPrinter LiteralPrinter;
	private final ReferencePrinter ReferencePrinter;

	@Inject
	public MethodReferenceExpressionChildPrinter(jamopp.printer.implementation.LiteralPrinter literalPrinter,
			jamopp.printer.implementation.ReferencePrinter referencePrinter) {
		super();
		LiteralPrinter = literalPrinter;
		ReferencePrinter = referencePrinter;
	}

	@Override
	public void print(MethodReferenceExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			LiteralPrinter.print((Literal) element, writer);
		} else {
			ReferencePrinter.print((Reference) element, writer);
		}
	}

}
