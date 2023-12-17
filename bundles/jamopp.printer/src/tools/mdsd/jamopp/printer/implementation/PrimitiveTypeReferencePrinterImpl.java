package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.references.PrimitiveTypeReference;
import org.emftext.language.java.types.PrimitiveType;

import com.google.inject.Inject;

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
