package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.PrimitiveTypeReference;

import jamopp.printer.interfaces.Printer;

class PrimitiveTypeReferencePrinter implements Printer<PrimitiveTypeReference>{

	private final PrimitiveTypePrinter PrimitiveTypePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	
	public void print(PrimitiveTypeReference element, BufferedWriter writer)
			throws IOException {
		PrimitiveTypePrinter.print(element.getPrimitiveType(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
