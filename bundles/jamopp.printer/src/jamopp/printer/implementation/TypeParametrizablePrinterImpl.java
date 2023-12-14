package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParametrizable;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.TypeParameterPrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;

public class TypeParametrizablePrinterImpl implements TypeParametrizablePrinterInt {

	private final TypeParameterPrinterInt TypeParameterPrinter;

	@Inject
	public TypeParametrizablePrinterImpl(TypeParameterPrinterInt typeParameterPrinter) {
		TypeParameterPrinter = typeParameterPrinter;
	}

	@Override
	public void print(TypeParametrizable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeParameters().isEmpty()) {
			writer.append("<");
			TypeParameterPrinter.print(element.getTypeParameters().get(0), writer);
			for (int index = 1; index < element.getTypeParameters().size(); index++) {
				writer.append(", ");
				TypeParameterPrinter.print(element.getTypeParameters().get(index), writer);
			}
			writer.append("> ");
		}
	}

}
