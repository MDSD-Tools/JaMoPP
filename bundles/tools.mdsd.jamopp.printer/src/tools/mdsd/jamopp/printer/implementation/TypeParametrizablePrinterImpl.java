package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeParametrizablePrinterImpl implements Printer<TypeParametrizable> {

	private final Printer<TypeParameter> typeParameterPrinter;

	@Inject
	public TypeParametrizablePrinterImpl(final Printer<TypeParameter> typeParameterPrinter) {
		this.typeParameterPrinter = typeParameterPrinter;
	}

	@Override
	public void print(final TypeParametrizable element, final BufferedWriter writer) throws IOException {
		if (!element.getTypeParameters().isEmpty()) {
			writer.append("<");
			typeParameterPrinter.print(element.getTypeParameters().get(0), writer);
			for (var index = 1; index < element.getTypeParameters().size(); index++) {
				writer.append(", ");
				typeParameterPrinter.print(element.getTypeParameters().get(index), writer);
			}
			writer.append("> ");
		}
	}

}
