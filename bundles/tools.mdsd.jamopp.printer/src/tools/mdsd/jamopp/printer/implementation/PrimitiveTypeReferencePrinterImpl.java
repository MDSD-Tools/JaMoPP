package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class PrimitiveTypeReferencePrinterImpl implements Printer<PrimitiveTypeReference> {

	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<PrimitiveType> primitiveTypePrinter;

	@Inject
	public PrimitiveTypeReferencePrinterImpl(Printer<PrimitiveType> primitiveTypePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.primitiveTypePrinter = primitiveTypePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(PrimitiveTypeReference element, BufferedWriter writer) throws IOException {
		this.primitiveTypePrinter.print(element.getPrimitiveType(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
