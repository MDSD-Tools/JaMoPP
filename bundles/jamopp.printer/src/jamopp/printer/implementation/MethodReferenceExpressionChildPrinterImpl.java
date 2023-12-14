package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.MethodReferenceExpressionChild;
import org.emftext.language.java.literals.Literal;
import org.emftext.language.java.references.Reference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionChildPrinterImpl implements Printer<MethodReferenceExpressionChild> {

	private final Printer<Literal> literalPrinter;
	private final Printer<Reference> referencePrinter;

	@Inject
	public MethodReferenceExpressionChildPrinterImpl(Printer<Literal> literalPrinter,
			Printer<Reference> referencePrinter) {
		this.literalPrinter = literalPrinter;
		this.referencePrinter = referencePrinter;
	}

	@Override
	public void print(MethodReferenceExpressionChild element, BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			this.literalPrinter.print((Literal) element, writer);
		} else {
			this.referencePrinter.print((Reference) element, writer);
		}
	}

}
