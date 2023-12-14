package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionChildPrinterImpl implements Printer<MethodReferenceExpressionChild> {

	private final Printer<Literal> LiteralPrinter;
	private final Printer<Reference> ReferencePrinter;

	@Inject
	public MethodReferenceExpressionChildPrinterImpl(Printer<Literal> literalPrinter,
			Printer<Reference> referencePrinter) {
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
