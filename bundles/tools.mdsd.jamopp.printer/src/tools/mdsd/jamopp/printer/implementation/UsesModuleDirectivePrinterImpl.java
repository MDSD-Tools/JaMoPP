package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UsesModuleDirectivePrinterImpl implements Printer<UsesModuleDirective> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public UsesModuleDirectivePrinterImpl(final Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(final UsesModuleDirective element, final BufferedWriter writer) throws IOException {
		writer.append("uses ");
		typeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}

}
