package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.LiteralPrinterInt;
import jamopp.printer.interfaces.printer.MethodReferenceExpressionChildPrinterInt;
import jamopp.printer.interfaces.printer.ReferencePrinterInt;

public class MethodReferenceExpressionChildPrinterImpl implements MethodReferenceExpressionChildPrinterInt {

	private final LiteralPrinterInt LiteralPrinter;
	private final ReferencePrinterInt ReferencePrinter;

	@Inject
	public MethodReferenceExpressionChildPrinterImpl(LiteralPrinterInt literalPrinter,
			ReferencePrinterInt referencePrinter) {
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
