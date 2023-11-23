package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.containers.Module;
import org.emftext.language.java.modules.ExportsModuleDirective;
import org.emftext.language.java.modules.ModuleDirective;
import org.emftext.language.java.modules.OpensModuleDirective;
import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.modules.RequiresModuleDirective;
import org.emftext.language.java.modules.UsesModuleDirective;

public class ModulePrinter {

	static void printModule(org.emftext.language.java.containers.Module element, BufferedWriter writer) throws IOException {
		writer.append("module ");
		if (element.getOpen() != null) {
			writer.append("open ");
		}
		writer.append(element.getNamespacesAsString() + " {\n");
		for (ModuleDirective dir : element.getTarget()) {
			if (dir instanceof UsesModuleDirective) {
				UsesModuleDirectivePrinter.printUsesModuleDirective((UsesModuleDirective) dir, writer);
			} else if (dir instanceof ProvidesModuleDirective) {
				ProvidesModuleDirectivePrinter.printProvidesModuleDirective((ProvidesModuleDirective) dir, writer);
			} else if (dir instanceof RequiresModuleDirective) {
				RequiresModuleDirectivePrinter.printRequiresModuleDirective((RequiresModuleDirective) dir, writer);
			} else if (dir instanceof OpensModuleDirective) {
				OpensModuleDirectivePrinter.printOpensModuleDirective((OpensModuleDirective) dir, writer);
			} else {
				ExportsModuleDirectivePrinter.printExportsModuleDirective((ExportsModuleDirective) dir, writer);
			}
		}
		writer.append("}\n");
	}

}
