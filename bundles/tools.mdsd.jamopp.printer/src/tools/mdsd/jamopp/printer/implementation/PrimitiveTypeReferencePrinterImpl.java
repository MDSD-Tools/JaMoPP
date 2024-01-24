package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrimitiveTypeReferencePrinterImpl implements Printer<PrimitiveTypeReference> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<PrimitiveType> primitiveTypePrinter;

	@Inject
	public PrimitiveTypeReferencePrinterImpl(final Printer<PrimitiveType> primitiveTypePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.primitiveTypePrinter = primitiveTypePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final PrimitiveTypeReference element, final BufferedWriter writer) throws IOException {
		primitiveTypePrinter.print(element.getPrimitiveType(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
