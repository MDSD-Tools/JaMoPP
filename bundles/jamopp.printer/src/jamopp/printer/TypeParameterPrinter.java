package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.TypeParameter;

class TypeParameterPrinter {

	static void print(TypeParameter element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getName());
		if (!element.getExtendTypes().isEmpty()) {
			writer.append(" extends ");
			TypeReferencePrinter.print(element.getExtendTypes().get(0), writer);
			for (int index = 1; index < element.getExtendTypes().size(); index++) {
				writer.append(" & ");
				TypeReferencePrinter.print(element.getExtendTypes().get(index), writer);
			}
		}
	}

}
