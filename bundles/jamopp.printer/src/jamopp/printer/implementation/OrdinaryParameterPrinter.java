package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.OrdinaryParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.OrdinaryParameterPrinterInt;

public class OrdinaryParameterPrinter implements OrdinaryParameterPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	private final ArrayDimensionsPrinter ArrayDimensionsPrinter;

	@Inject
	public OrdinaryParameterPrinter(
			AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter,
			ArrayDimensionsPrinter arrayDimensionsPrinter) {
		super();
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
