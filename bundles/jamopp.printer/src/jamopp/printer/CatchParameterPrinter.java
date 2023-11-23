package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.types.TypeReference;

class CatchParameterPrinter {

	static void print(CatchParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				TypeReferencePrinter.print(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

}
