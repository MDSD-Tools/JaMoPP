package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.UsesModuleDirective;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class UsesModuleDirectivePrinterImpl implements Printer<UsesModuleDirective> {

	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public UsesModuleDirectivePrinterImpl(Printer<TypeReference> typeReferencePrinter) {
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}



}
