package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.parameters.OrdinaryParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class OrdinaryParameterPrinterImpl implements OrdinaryParameterPrinterInt {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;

	@Inject
	public OrdinaryParameterPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
	}

	@Override
	public void print(OrdinaryParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		TypeArgumentablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		writer.append(" " + element.getName());
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
	}

}
