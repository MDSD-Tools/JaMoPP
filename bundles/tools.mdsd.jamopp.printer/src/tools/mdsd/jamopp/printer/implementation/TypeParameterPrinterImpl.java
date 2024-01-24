package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeParameterPrinterImpl implements Printer<TypeParameter> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public TypeParameterPrinterImpl(final Printer<Annotable> annotablePrinter,
			final Printer<TypeReference> typeReferencePrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(final TypeParameter element, final BufferedWriter writer) throws IOException {
		annotablePrinter.print(element, writer);
		writer.append(element.getName());
		if (!element.getExtendTypes().isEmpty()) {
			writer.append(" extends ");
			typeReferencePrinter.print(element.getExtendTypes().get(0), writer);
			for (var index = 1; index < element.getExtendTypes().size(); index++) {
				writer.append(" & ");
				typeReferencePrinter.print(element.getExtendTypes().get(index), writer);
			}
		}
	}

}
