package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.OpensModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class OpensModuleDirectivePrinter implements Printer<OpensModuleDirective> {

	private final RemainingAccessProvidingModuleDirectivePrinter RemainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public OpensModuleDirectivePrinter(
			jamopp.printer.implementation.RemainingAccessProvidingModuleDirectivePrinter remainingAccessProvidingModuleDirectivePrinter) {
		super();
		RemainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	public void print(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
