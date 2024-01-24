package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.modules.AccessProvidingModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ExportsModuleDirective;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExportsModuleDirectivePrinterImpl implements Printer<ExportsModuleDirective> {

	private final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public ExportsModuleDirectivePrinterImpl(
			final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter) {
		this.remainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(final ExportsModuleDirective element, final BufferedWriter writer) throws IOException {
		writer.append("exports ");
		remainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
