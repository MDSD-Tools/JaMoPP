package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.VariableLengthParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.VariableLengthParameterPrinterInt;

public class VariableLengthParameterPrinter implements VariableLengthParameterPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;
	private final AnnotablePrinter AnnotablePrinter;

	@Inject
	public VariableLengthParameterPrinter(
			AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			ArrayDimensionsPrinter arrayDimensionsPrinter,
			AnnotablePrinter annotablePrinter) {
		super();
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
