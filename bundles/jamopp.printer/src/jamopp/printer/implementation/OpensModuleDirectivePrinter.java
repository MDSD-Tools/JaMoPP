package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.OpensModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.OpensModuleDirectivePrinterInt;

class OpensModuleDirectivePrinter implements Printer<OpensModuleDirective>, OpensModuleDirectivePrinterInt {

	private final RemainingAccessProvidingModuleDirectivePrinter RemainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public OpensModuleDirectivePrinter(
			jamopp.printer.implementation.RemainingAccessProvidingModuleDirectivePrinter remainingAccessProvidingModuleDirectivePrinter) {
		super();
		RemainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
