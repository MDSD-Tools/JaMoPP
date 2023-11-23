package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.parameters.ReceiverParameter;

public class ReceiverParameterPrinter {

	static void printReceiverParameter(ReceiverParameter element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		TypeReferencePrinter.printTypeReference(element.getTypeReference(), writer);
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
		writer.append(" ");
		if (element.getOuterTypeReference() != null) {
			TypeReferencePrinter.printTypeReference(element.getOuterTypeReference(), writer);
			writer.append(".");
		}
		writer.append("this");
	}

}
