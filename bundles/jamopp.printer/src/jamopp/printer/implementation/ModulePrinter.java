package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.modules.ExportsModuleDirective;
import org.emftext.language.java.modules.ModuleDirective;
import org.emftext.language.java.modules.OpensModuleDirective;
import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.modules.RequiresModuleDirective;
import org.emftext.language.java.modules.UsesModuleDirective;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ExportsModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.ModulePrinterInt;
import jamopp.printer.interfaces.printer.OpensModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.ProvidesModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.RequiresModuleDirectivePrinterInt;
import jamopp.printer.interfaces.printer.UsesModuleDirectivePrinterInt;

public class ModulePrinter implements ModulePrinterInt {

	private final UsesModuleDirectivePrinterInt UsesModuleDirectivePrinter;
	private final ProvidesModuleDirectivePrinterInt ProvidesModuleDirectivePrinter;
	private final RequiresModuleDirectivePrinterInt RequiresModuleDirectivePrinter;
	private final OpensModuleDirectivePrinterInt OpensModuleDirectivePrinter;
	private final ExportsModuleDirectivePrinterInt ExportsModuleDirectivePrinter;

	@Inject
	public ModulePrinter(UsesModuleDirectivePrinterInt usesModuleDirectivePrinter,
			ProvidesModuleDirectivePrinterInt providesModuleDirectivePrinter,
			RequiresModuleDirectivePrinterInt requiresModuleDirectivePrinter,
			OpensModuleDirectivePrinterInt opensModuleDirectivePrinter,
			ExportsModuleDirectivePrinterInt exportsModuleDirectivePrinter) {
		UsesModuleDirectivePrinter = usesModuleDirectivePrinter;
		ProvidesModuleDirectivePrinter = providesModuleDirectivePrinter;
		RequiresModuleDirectivePrinter = requiresModuleDirectivePrinter;
		OpensModuleDirectivePrinter = opensModuleDirectivePrinter;
		ExportsModuleDirectivePrinter = exportsModuleDirectivePrinter;
	}

	@Override
	public void print(org.emftext.language.java.containers.Module element, BufferedWriter writer) throws IOException {
		writer.append("module ");
		if (element.getOpen() != null) {
			writer.append("open ");
		}
		writer.append(element.getNamespacesAsString() + " {\n");
		for (ModuleDirective dir : element.getTarget()) {
			if (dir instanceof UsesModuleDirective) {
				UsesModuleDirectivePrinter.print((UsesModuleDirective) dir, writer);
			} else if (dir instanceof ProvidesModuleDirective) {
				ProvidesModuleDirectivePrinter.print((ProvidesModuleDirective) dir, writer);
			} else if (dir instanceof RequiresModuleDirective) {
				RequiresModuleDirectivePrinter.print((RequiresModuleDirective) dir, writer);
			} else if (dir instanceof OpensModuleDirective) {
				OpensModuleDirectivePrinter.print((OpensModuleDirective) dir, writer);
			} else {
				ExportsModuleDirectivePrinter.print((ExportsModuleDirective) dir, writer);
			}
		}
		writer.append("}\n");
	}

}
