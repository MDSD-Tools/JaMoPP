package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.annotations.Annotable;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ReceiverParameterPrinterImpl implements Printer<ReceiverParameter> {

	private final Printer<Annotable> annotablePrinter;
	private final Printer<TypeArgumentable> typeArgumentablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ReceiverParameterPrinterImpl(final Printer<Annotable> annotablePrinter,
			final Printer<TypeReference> typeReferencePrinter,
			final Printer<TypeArgumentable> typeArgumentablePrinter) {
		this.annotablePrinter = annotablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.typeArgumentablePrinter = typeArgumentablePrinter;
	}

	@Override
	public void print(final ReceiverParameter element, final BufferedWriter writer) throws IOException {
		annotablePrinter.print(element, writer);
		typeReferencePrinter.print(element.getTypeReference(), writer);
		typeArgumentablePrinter.print(element, writer);
		writer.append(" ");
		if (element.getOuterTypeReference() != null) {
			typeReferencePrinter.print(element.getOuterTypeReference(), writer);
			writer.append(".");
		}
		writer.append("this");
	}

}
