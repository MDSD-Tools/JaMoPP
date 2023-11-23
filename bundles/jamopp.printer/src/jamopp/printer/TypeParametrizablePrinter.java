package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParametrizable;

public class TypeParametrizablePrinter {

	static void printTypeParametrizable(TypeParametrizable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeParameters().isEmpty()) {
			writer.append("<");
			TypeParameterPrinter.printTypeParameter(element.getTypeParameters().get(0), writer);
			for (int index = 1; index < element.getTypeParameters().size(); index++) {
				writer.append(", ");
				TypeParameterPrinter.printTypeParameter(element.getTypeParameters().get(index), writer);
			}
			writer.append("> ");
		}
	}

}
