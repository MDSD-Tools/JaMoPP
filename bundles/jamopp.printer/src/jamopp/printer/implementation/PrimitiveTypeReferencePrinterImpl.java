package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.PrimitiveTypeReference;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypePrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypeReferencePrinterInt;

public class PrimitiveTypeReferencePrinterImpl implements PrimitiveTypeReferencePrinterInt {

	private final PrimitiveTypePrinterInt PrimitiveTypePrinter;
	private final ArrayDimensionsPrinterInt ArrayDimensionsPrinter;

	@Inject
	public PrimitiveTypeReferencePrinterImpl(PrimitiveTypePrinterInt primitiveTypePrinter,
			ArrayDimensionsPrinterInt arrayDimensionsPrinter) {
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
