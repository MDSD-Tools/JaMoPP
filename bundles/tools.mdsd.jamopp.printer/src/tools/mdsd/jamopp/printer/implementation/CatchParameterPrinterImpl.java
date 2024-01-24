package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CatchParameterPrinterImpl implements Printer<CatchParameter> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public CatchParameterPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeReference> typeReferencePrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(final CatchParameter element, final BufferedWriter writer) throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		typeReferencePrinter.print(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (final TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				typeReferencePrinter.print(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

}
