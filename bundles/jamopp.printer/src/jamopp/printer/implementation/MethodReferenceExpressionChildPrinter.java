package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

import jamopp.printer.interfaces.Printer;

class MethodReferenceExpressionChildPrinter implements Printer<MethodReferenceExpressionChild> {

	private final LiteralPrinter LiteralPrinter;
	private final ReferencePrinter ReferencePrinter;
	
	public void print(MethodReferenceExpressionChild element,
			BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			LiteralPrinter.print((Literal) element, writer);
		} else {
			ReferencePrinter.print((Reference) element, writer);
		}
	}

}
