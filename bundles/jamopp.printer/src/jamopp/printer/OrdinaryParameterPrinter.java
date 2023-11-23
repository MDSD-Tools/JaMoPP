package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;

public class OrdinaryParameterPrinter {

	static void printOrdinaryParameter(OrdinaryParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
	}

}
