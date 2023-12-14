package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
