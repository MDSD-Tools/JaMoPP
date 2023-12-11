package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeArgumentable;

import jamopp.printer.interfaces.Printer;

class TypeArgumentablePrinter implements Printer<TypeArgumentable>{

	private final TypeArgumentPrinter TypeArgumentPrinter;
	
	public void print(TypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.print(element.getTypeArguments().get(0), writer);
			for (int index = 1; index < element.getTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.print(element.getTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
