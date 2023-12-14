package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.AccessProvidingModuleDirective;
import org.emftext.language.java.modules.OpensModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class OpensModuleDirectivePrinterImpl implements Printer<OpensModuleDirective> {

	private final Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public OpensModuleDirectivePrinterImpl(
			Printer<AccessProvidingModuleDirective> remainingAccessProvidingModuleDirectivePrinter) {
		this.remainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(OpensModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("opens ");
		this.remainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
