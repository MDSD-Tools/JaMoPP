package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.expressions.MethodReferenceExpressionChild;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.references.Reference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

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
