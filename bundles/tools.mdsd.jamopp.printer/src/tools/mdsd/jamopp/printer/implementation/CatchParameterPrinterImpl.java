package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CatchParameterPrinterImpl implements Printer<CatchParameter> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public CatchParameterPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeReference> typeReferencePrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(CatchParameter element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				this.typeReferencePrinter.print(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

}
