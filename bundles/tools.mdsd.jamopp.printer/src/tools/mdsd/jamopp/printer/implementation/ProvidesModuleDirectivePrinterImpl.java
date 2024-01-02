package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ProvidesModuleDirectivePrinterImpl implements Printer<ProvidesModuleDirective> {

	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ProvidesModuleDirectivePrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		this.typeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(ProvidesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("provides ");
		this.typeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(" with ");
		for (var index = 0; index < element.getServiceProviders().size(); index++) {
			var ref = element.getServiceProviders().get(index);
			this.typeReferencePrinter.print(ref, writer);
			if (index < element.getServiceProviders().size() - 1) {
				writer.append(".");
			}
		}
		writer.append(";\n");
	}

}
