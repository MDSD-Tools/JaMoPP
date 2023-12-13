package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.UsesModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.UsesModuleDirectivePrinterInt;

class UsesModuleDirectivePrinter implements Printer<UsesModuleDirective>, UsesModuleDirectivePrinterInt {

	private final TypeReferencePrinter TypeReferencePrinter;

	@Inject
	public UsesModuleDirectivePrinter(jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter) {
		super();
		TypeReferencePrinter = typeReferencePrinter;
	}

	@Override
	public void print(UsesModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("uses ");
		TypeReferencePrinter.print(element.getTypeReference(), writer);
		writer.append(";\n");
	}

}
