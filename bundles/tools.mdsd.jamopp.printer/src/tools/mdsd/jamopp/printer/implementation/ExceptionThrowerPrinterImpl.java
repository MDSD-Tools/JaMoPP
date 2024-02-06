package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.members.ExceptionThrower;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExceptionThrowerPrinterImpl implements Printer<ExceptionThrower> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ExceptionThrowerPrinterImpl(final Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(final ExceptionThrower element, final BufferedWriter writer) throws IOException {
		if (!element.getExceptions().isEmpty()) {
			writer.append("throws ");
			typeReferencePrinter.print(element.getExceptions().get(0), writer);
			for (var index = 1; index < element.getExceptions().size(); index++) {
				writer.append(", ");
				typeReferencePrinter.print(element.getExceptions().get(index), writer);
			}
		}
	}

}
