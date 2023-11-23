package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeArgumentable;

class TypeArgumentablePrinter {

	static void print(TypeArgumentable element, BufferedWriter writer) throws IOException {
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
