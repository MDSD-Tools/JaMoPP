package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.OpensModuleDirective;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.OpensModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.RemainingAccessProvidingModuleDirectivePrinterInt;

public class OpensModuleDirectivePrinterImpl implements OpensModuleDirectivePrinterInt {

	private final RemainingAccessProvidingModuleDirectivePrinterInt RemainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public OpensModuleDirectivePrinterImpl(
			RemainingAccessProvidingModuleDirectivePrinterInt remainingAccessProvidingModuleDirectivePrinter) {
		RemainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
