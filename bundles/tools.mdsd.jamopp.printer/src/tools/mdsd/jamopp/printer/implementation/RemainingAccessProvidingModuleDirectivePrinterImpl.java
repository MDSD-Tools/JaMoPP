package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modules.AccessProvidingModuleDirective;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class RemainingAccessProvidingModuleDirectivePrinterImpl implements Printer<AccessProvidingModuleDirective> {

	@Override
	public void print(final AccessProvidingModuleDirective element, final BufferedWriter writer) throws IOException {
		writer.append(element.getAccessablePackage().getNamespacesAsString());
		if (!element.getModules().isEmpty()) {
			writer.append(" to ");
			writer.append(element.getModules().get(0).getTarget().getNamespacesAsString());
			for (var index = 1; index < element.getModules().size(); index++) {
				writer.append(", ");
				writer.append(element.getModules().get(index).getTarget().getNamespacesAsString());
			}
		}
		writer.append(";\n");
	}

}
