package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.AccessProvidingModuleDirective;

class RemainingAccessProvidingModuleDirectivePrinter {

	static void print(AccessProvidingModuleDirective element,
			BufferedWriter writer) throws IOException {
		writer.append(element.getAccessablePackage().getNamespacesAsString());
		if (!element.getModules().isEmpty()) {
			writer.append(" to ");
			writer.append(element.getModules().get(0).getTarget().getNamespacesAsString());
			for (int index = 1; index < element.getModules().size(); index++) {
				writer.append(", ");
				writer.append(element.getModules().get(index).getTarget().getNamespacesAsString());
			}
		}
		writer.append(";\n");
	}

}
