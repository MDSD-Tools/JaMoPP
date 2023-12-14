package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ProvidesModuleDirectivePrinterImpl implements Printer<ProvidesModuleDirective> {

	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public ProvidesModuleDirectivePrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(ProvidesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("provides ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(" with ");
		for (var index = 0; index < element.getServiceProviders().size(); index++) {
			var ref = element.getServiceProviders().get(index);
			TypeReferencePrinter.print(ref, writer);
			if (index < element.getServiceProviders().size() - 1) {
				writer.append(".");
			}
		}
		writer.append(";\n");
	}

}
