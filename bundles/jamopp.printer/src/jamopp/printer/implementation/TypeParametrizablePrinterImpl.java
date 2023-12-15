package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.generics.TypeParametrizable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class TypeParametrizablePrinterImpl implements Printer<TypeParametrizable> {

	private final Printer<TypeParameter> typeParameterPrinter;

	@Inject
	public TypeParametrizablePrinterImpl(Printer<TypeParameter> typeParameterPrinter) {
		this.typeParameterPrinter = typeParameterPrinter;
	}

	@Override
	public void print(TypeParametrizable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeParameters().isEmpty()) {
			writer.append("<");
			this.typeParameterPrinter.print(element.getTypeParameters().get(0), writer);
			for (var index = 1; index < element.getTypeParameters().size(); index++) {
				writer.append(", ");
				this.typeParameterPrinter.print(element.getTypeParameters().get(index), writer);
			}
			writer.append("> ");
		}
	}

}