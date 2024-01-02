package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.generics.TypeArgument;
import tools.mdsd.jamopp.model.java.generics.TypeArgumentable;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class TypeArgumentablePrinterImpl implements Printer<TypeArgumentable> {

	private final Printer<TypeArgument> typeArgumentPrinter;

	@Inject
	public TypeArgumentablePrinterImpl(Printer<TypeArgument> typeArgumentPrinter) {
		this.typeArgumentPrinter = typeArgumentPrinter;
	}

	@Override
	public void print(TypeArgumentable element, BufferedWriter writer) throws IOException {
		if (!element.getTypeArguments().isEmpty()) {
			writer.append("<");
			this.typeArgumentPrinter.print(element.getTypeArguments().get(0), writer);
			for (var index = 1; index < element.getTypeArguments().size(); index++) {
				writer.append(", ");
				this.typeArgumentPrinter.print(element.getTypeArguments().get(index), writer);
			}
			writer.append(">");
		}
	}

}
