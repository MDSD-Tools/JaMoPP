package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExportsModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.RemainingAccessProvidingModuleDirectivePrinterInt;

public class ExportsModuleDirectivePrinter implements ExportsModuleDirectivePrinterInt {

	private final RemainingAccessProvidingModuleDirectivePrinterInt RemainingAccessProvidingModuleDirectivePrinter;

	@Inject
	public ExportsModuleDirectivePrinter(
			RemainingAccessProvidingModuleDirectivePrinterInt remainingAccessProvidingModuleDirectivePrinter) {
		RemainingAccessProvidingModuleDirectivePrinter = remainingAccessProvidingModuleDirectivePrinter;
	}

	@Override
	public void print(ExportsModuleDirective element, BufferedWriter writer) throws IOException {
		writer.append("exports ");
		RemainingAccessProvidingModuleDirectivePrinter.print(element, writer);
	}

}
