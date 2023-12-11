package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParametrizable;

import jamopp.printer.interfaces.Printer;

class TypeParametrizablePrinter implements Printer<TypeParametrizable>{

	private final TypeParameterPrinter TypeParameterPrinter;
	
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
