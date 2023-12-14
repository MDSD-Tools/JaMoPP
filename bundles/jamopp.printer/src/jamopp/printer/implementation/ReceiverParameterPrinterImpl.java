package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.Annotable;
import org.emftext.language.java.generics.TypeArgumentable;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ReceiverParameterPrinterImpl implements Printer<ReceiverParameter> {

	private final Printer<Annotable> AnnotablePrinter;
	private final Printer<TypeArgumentable> TypeArgumentablePrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public ReceiverParameterPrinterImpl(Printer<Annotable> annotablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<TypeArgumentable> typeArgumentablePrinter) {
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
