package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MethodReferenceExpressionChildPrinterImpl implements Printer<MethodReferenceExpressionChild> {

	private final Printer<Literal> literalPrinter;
	private final Printer<Reference> referencePrinter;

	@Inject
	public MethodReferenceExpressionChildPrinterImpl(final Printer<Literal> literalPrinter,
			final Printer<Reference> referencePrinter) {
		this.literalPrinter = literalPrinter;
		this.referencePrinter = referencePrinter;
	}

	@Override
	public void print(final MethodReferenceExpressionChild element, final BufferedWriter writer) throws IOException {
		if (element instanceof Literal) {
			literalPrinter.print((Literal) element, writer);
		} else {
			referencePrinter.print((Reference) element, writer);
		}
	}

}
