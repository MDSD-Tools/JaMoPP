package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modifiers.Static;
import org.emftext.language.java.modules.RequiresModuleDirective;

import jamopp.printer.interfaces.Printer;

class RequiresModuleDirectivePrinter implements Printer<RequiresModuleDirective>{

	public void print(RequiresModuleDirective element, BufferedWriter writer)
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
