package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Implementor;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ImplementorPrinterImpl implements Printer<Implementor> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ImplementorPrinterImpl(final Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(final Implementor element, final BufferedWriter writer) throws IOException {
		if (!element.getImplements().isEmpty()) {
			writer.append("implements ");
			typeReferencePrinter.print(element.getImplements().get(0), writer);
			for (var index = 1; index < element.getImplements().size(); index++) {
				writer.append(", ");
				typeReferencePrinter.print(element.getImplements().get(index), writer);
			}
			writer.append(" ");
		}
	}

}
