package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.ReceiverParameter;

class ReceiverParameterPrinter {

	static void print(ReceiverParameter element, BufferedWriter writer) throws IOException {
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
