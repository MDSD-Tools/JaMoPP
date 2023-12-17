package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.AccessProvidingModuleDirective;
import org.emftext.language.java.modules.ExportsModuleDirective;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ExportsModuleDirectivePrinterImpl implements Printer<ExportsModuleDirective> {

	private final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public ExportsModuleDirectivePrinterImpl(
			Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter) {
		this.remainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		this.remainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
