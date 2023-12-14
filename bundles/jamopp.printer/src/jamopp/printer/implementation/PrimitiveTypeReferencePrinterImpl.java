package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.references.PrimitiveTypeReference;
import org.emftext.language.java.types.PrimitiveType;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class PrimitiveTypeReferencePrinterImpl implements Printer<PrimitiveTypeReference> {

	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<PrimitiveType> PrimitiveTypePrinter;

	@Inject
	public PrimitiveTypeReferencePrinterImpl(Printer<PrimitiveType> primitiveTypePrinter,
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
