package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.VariableLengthParameter;

public class VariableLengthParameterPrinter {

	static void printVariableLengthParameter(VariableLengthParameter element, BufferedWriter writer)
			throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.printArrayDimensions(element.getArrayDimensionsAfter(), writer);
		writer.append(" ");
		AnnotablePrinter.printAnnotable(element, writer);
		writer.append(" ..." + element.getName());
	}

}
