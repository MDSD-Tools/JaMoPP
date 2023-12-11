package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.ReceiverParameter;

import jamopp.printer.interfaces.Printer;

class ReceiverParameterPrinter implements Printer<ReceiverParameter>{

	private final AnnotablePrinter AnnotablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final TypeArgumentablePrinter TypeArgumentablePrinter;
	
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
