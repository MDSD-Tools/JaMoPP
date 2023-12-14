package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotablePrinterInt;
import jamopp.printer.interfaces.printer.ReceiverParameterPrinterInt;
import jamopp.printer.interfaces.printer.TypeArgumentablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ReceiverParameterPrinter implements ReceiverParameterPrinterInt {

	private final AnnotablePrinterInt AnnotablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final TypeArgumentablePrinterInt TypeArgumentablePrinter;

	@Inject
	public ReceiverParameterPrinter(AnnotablePrinterInt annotablePrinter, TypeReferencePrinterInt typeReferencePrinter,
			TypeArgumentablePrinterInt typeArgumentablePrinter) {
		AnnotablePrinter = annotablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		TypeArgumentablePrinter = typeArgumentablePrinter;
	}

	@Override
	public void print(ReceiverParameter element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		TypeArgumentablePrinter.print(element, writer);
		writer.append(" ");
		if (element.getOuterTypeReference() != null) {
			TypeReferencePrinter.print(element.getOuterTypeReference(), writer);
			writer.append(".");
		}
		writer.append("this");
	}

}
