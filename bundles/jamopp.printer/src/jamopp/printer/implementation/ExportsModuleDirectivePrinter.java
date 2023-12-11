package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ExportsModuleDirectivePrinter implements Printer<ExportsModuleDirective> {

	private final RemainingAccessProvidingModuleDirectivePrinter RemainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public ExportsModuleDirectivePrinter(
			jamopp.printer.implementation.RemainingAccessProvidingModuleDirectivePrinter remainingAccessProvidingModuleDirectivePrinter) {
		super();
		RemainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	public void print(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
