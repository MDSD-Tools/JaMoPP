package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.modules.AccessProvidingModuleDirective;
import tools.mdsd.jamopp.model.java.modules.OpensModuleDirective;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class OpensModuleDirectivePrinterImpl implements Printer<OpensModuleDirective> {

	private final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public OpensModuleDirectivePrinterImpl(
			final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter) {
		this.remainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(final OpensModuleDirective element, final BufferedWriter writer) throws IOException {
		writer.append("opens ");
		remainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
