package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.references.PrimitiveTypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypePrinterInt;
import jamopp.printer.interfaces.printer.PrimitiveTypeReferencePrinterInt;

public class PrimitiveTypeReferencePrinterImpl implements PrimitiveTypeReferencePrinterInt {

	private final PrimitiveTypePrinterInt PrimitiveTypePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;

	@Inject
	public PrimitiveTypeReferencePrinterImpl(PrimitiveTypePrinterInt primitiveTypePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
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
