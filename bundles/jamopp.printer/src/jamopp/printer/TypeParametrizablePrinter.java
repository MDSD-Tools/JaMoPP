package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParametrizable;

class TypeParametrizablePrinter {

	static void print(TypeParametrizable element, BufferedWriter writer) throws IOException {
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
