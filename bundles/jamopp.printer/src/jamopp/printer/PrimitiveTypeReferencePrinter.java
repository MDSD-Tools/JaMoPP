package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.PrimitiveTypeReference;

class PrimitiveTypeReferencePrinter {

	static void print(PrimitiveTypeReference element, BufferedWriter writer)
			throws IOException {
		PrimitiveTypePrinter.print(element.getPrimitiveType(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
