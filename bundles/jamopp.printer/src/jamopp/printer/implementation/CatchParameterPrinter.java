package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class CatchParameterPrinter implements Printer<CatchParameter> {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;

	@Inject
	public CatchParameterPrinter(
			jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter) {
		super();
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
	}

	public void print(CatchParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				TypeReferencePrinter.print(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

}
