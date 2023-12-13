package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ReceiverParameterPrinterInt;

class ReceiverParameterPrinter implements ReceiverParameterPrinterInt {

	private final AnnotablePrinter AnnotablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;

	@Inject
	public ReceiverParameterPrinter(AnnotablePrinter annotablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.TypeArgumentablePrinter typeArgumentablePrinter) {
		super();
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
