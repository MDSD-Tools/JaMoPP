package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.parameters.VariableLengthParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class VariableLengthParameterPrinterImpl implements Printer<VariableLengthParameter> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<Annotable> annotablePrinter;
	private final Printer<List<ArrayDimension>> arrayDimensionsPrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public VariableLengthParameterPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeReference> typeReferencePrinter, Printer<TypeArgumentable> typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Annotable> annotablePrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
		this.arrayDimensionsPrinter = arrayDimensionsPrinter;
		this.annotablePrinter = annotablePrinter;
	}

	@Override
	public void print(VariableLengthParameter element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		this.typeArgumentablePrinter.print(element, writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		this.arrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		writer.append(" ");
		this.annotablePrinter.print(element, writer);
		writer.append(" ..." + element.getName());
	}

}
