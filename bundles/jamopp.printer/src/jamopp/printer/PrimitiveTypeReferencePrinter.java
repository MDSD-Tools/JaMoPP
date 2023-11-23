package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.PrimitiveTypeReference;

public class PrimitiveTypeReferencePrinter {

	static void printPrimitiveTypeReference(PrimitiveTypeReference element, BufferedWriter writer)
			throws IOException {
		PrimitiveTypePrinter.printPrimitiveType(element.getPrimitiveType(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
	}

}
