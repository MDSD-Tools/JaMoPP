package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.Implementor;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ImplementorPrinterImpl implements Printer<Implementor> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ImplementorPrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(Implementor element, BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			this.typeReferencePrinter.print(element.getImplements().get(0), writer);
			for (var index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				this.typeReferencePrinter.print(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}

}
