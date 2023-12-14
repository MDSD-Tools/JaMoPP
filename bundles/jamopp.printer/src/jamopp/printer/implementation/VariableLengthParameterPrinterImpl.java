package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.arrays.ArrayDimension;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ArrayDimensionsPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;
import jamopp.printer.interfaces.printer.VariableLengthParameterPrinterInt;

public class VariableLengthParameterPrinterImpl implements VariableLengthParameterPrinterInt {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;
	private final Printer<List<ArrayDimension>> ArrayDimensionsPrinter;
	private final Printer<Annotable> AnnotablePrinter;

	@Inject
	public VariableLengthParameterPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			TypeReferencePrinterInt typeReferencePrinter, TypeArgumentablePrinterInt typeArgumentablePrinter,
			Printer<List<ArrayDimension>> arrayDimensionsPrinter, Printer<Annotable> annotablePrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
		ArrayDimensionsPrinter = arrayDimensionsPrinter;
		AnnotablePrinter = annotablePrinter;
	}

	@Override
	public void print(VariableLengthParameter element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		TypeArgumentablePrinter.print(element, writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsBefore(), writer);
		ArrayDimensionsPrinter.print(element.getArrayDimensionsAfter(), writer);
		writer.append(" ");
		AnnotablePrinter.print(element, writer);
		writer.append(" ..." + element.getName());
	}

}
