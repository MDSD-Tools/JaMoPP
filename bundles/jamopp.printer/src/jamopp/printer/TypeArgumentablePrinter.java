package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeArgumentable;

public class TypeArgumentablePrinter {

	static void printTypeArgumentable(TypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeArguments().isEmpty()) {
			writer.append("<");
			TypeArgumentPrinter.printTypeArgument(element.getTypeArguments().get(0), writer);
			for (int index = 1; index < element.getTypeArguments().size(); index++) {
				writer.append(", ");
				TypeArgumentPrinter.printTypeArgument(element.getTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
