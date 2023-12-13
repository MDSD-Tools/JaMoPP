package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.PrimitiveTypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.PrimitiveTypeReferencePrinterInt;

class PrimitiveTypeReferencePrinter implements Printer<PrimitiveTypeReference>, PrimitiveTypeReferencePrinterInt {

	private final PrimitiveTypePrinter PrimitiveTypePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public PrimitiveTypeReferencePrinter(jamopp.printer.implementation.PrimitiveTypePrinter primitiveTypePrinter,
			ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
		PrimitiveTypePrinter = primitiveTypePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(PrimitiveTypeReference element, BufferedWriter writer) throws IOException {
		PrimitiveTypePrinter.print(element.getPrimitiveType(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
