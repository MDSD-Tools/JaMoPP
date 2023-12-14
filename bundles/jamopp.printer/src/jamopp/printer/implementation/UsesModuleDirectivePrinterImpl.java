package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.UsesModuleDirective;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;
import jamopp.printer.interfaces.printer.UsesModuleDirectivePrinterInt;

public class UsesModuleDirectivePrinterImpl implements UsesModuleDirectivePrinterInt {

	private final TypeReferencePrinterInt TypeReferencePrinter;

	@Inject
	public UsesModuleDirectivePrinterImpl(TypeReferencePrinterInt typeReferencePrinter) {
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}

	

}
