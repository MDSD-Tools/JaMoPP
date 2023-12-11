package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;

import jamopp.printer.interfaces.Printer;

class ExportsModuleDirectivePrinter implements Printer<ExportsModuleDirective>{

	private final RemainingAccessProvidingModuleDirectivePrinter RemainingAccessProvidingModuleDirectivePrinter;
	
	public void print(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
