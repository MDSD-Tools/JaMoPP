package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.CatchParameter;
import org.emftext.language.java.types.TypeReference;

public class CatchParameterPrinter {

	static void printCatchParameter(CatchParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		if (!element.getTypeReferences().isEmpty()) {
			for (TypeReference ref : element.getTypeReferences()) {
				writer.append(" | ");
				TypeReferencePrinter.printTypeReference(ref, writer);
			}
		}
		writer.append(" " + element.getName());
	}

}
