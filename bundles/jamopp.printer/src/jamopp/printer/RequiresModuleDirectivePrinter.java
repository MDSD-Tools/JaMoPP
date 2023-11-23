package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.modules.RequiresModuleDirective;

public class RequiresModuleDirectivePrinter {

	static void printRequiresModuleDirective(RequiresModuleDirective element, BufferedWriter writer)
			throws IOException {
		writer.append("requires ");
		if (element.getModifier() != null) {
			if (element.getModifier() instanceof Static) {
				writer.append("static ");
			} else {
				writer.append("transitive ");
			}
		}
		writer.append(element.getRequiredModule().getTarget().getNamespacesAsString());
		writer.append(";\n");
	}

}
