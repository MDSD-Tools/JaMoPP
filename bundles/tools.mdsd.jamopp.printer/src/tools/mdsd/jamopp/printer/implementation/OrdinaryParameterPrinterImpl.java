package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.arrays.ArrayDimension;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class OrdinaryParameterPrinterImpl implements Printer<OrdinaryParameter> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public OrdinaryParameterPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeReference> typeReferencePrinter, final Printer<TypeArgumentable> typeArgumentablePrinter,
			final Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(final OrdinaryParameter element, final BufferedWriter writer) throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		typeReferencePrinter.print(element.getTypeReference(), writer);
		typeArgumentablePrinter.print(element, writer);
		arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
