package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class UsesModuleDirectivePrinterImpl implements Printer<UsesModuleDirective> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public UsesModuleDirectivePrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}

}
