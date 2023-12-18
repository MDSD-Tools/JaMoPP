package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.modules.ExportsModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ModuleDirective;
import tools.mdsd.jamopp.model.java.modules.OpensModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective;
import tools.mdsd.jamopp.model.java.modules.RequiresModuleDirective;
import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ModulePrinterImpl implements Printer<tools.mdsd.jamopp.model.java.containers.Module> {

	private final Printer<ExportsModuleDirective> exportsModuleDirectivePrinter;
	private final Printer<OpensModuleDirective> opensModuleDirectivePrinter;
	private final Printer<ProvidesModuleDirective> providesModuleDirectivePrinter;
	private final Printer<RequiresModuleDirective> requiresModuleDirectivePrinter;
	private final Printer<UsesModuleDirective> usesModuleDirectivePrinter;

	@Inject
	public ModulePrinterImpl(Printer<UsesModuleDirective> usesModuleDirectivePrinter,
			Printer<ProvidesModuleDirective> providesModuleDirectivePrinter,
			Printer<RequiresModuleDirective> requiresModuleDirectivePrinter,
			Printer<OpensModuleDirective> opensModuleDirectivePrinter,
			Printer<ExportsModuleDirective> exportsModuleDirectivePrinter) {
		this.usesModuleDirectivePrinter = usesModuleDirectivePrinter;
		this.providesModuleDirectivePrinter = providesModuleDirectivePrinter;
		this.requiresModuleDirectivePrinter = requiresModuleDirectivePrinter;
		this.opensModuleDirectivePrinter = opensModuleDirectivePrinter;
		this.exportsModuleDirectivePrinter = exportsModuleDirectivePrinter;
	}

	@Override
	public void print(tools.mdsd.jamopp.model.java.containers.Module element, BufferedWriter writer) throws IOException {
		writer.append("module ");
		if (element.getOpen() != null) {
			writer.append("open ");
		}
		writer.append(element.getNamespacesAsString() + " {\n");
		for (ModuleDirective dir : element.getTarget()) {
			if (dir instanceof UsesModuleDirective) {
				this.usesModuleDirectivePrinter.print((UsesModuleDirective) dir, writer);
			} else if (dir instanceof ProvidesModuleDirective) {
				this.providesModuleDirectivePrinter.print((ProvidesModuleDirective) dir, writer);
			} else if (dir instanceof RequiresModuleDirective) {
				this.requiresModuleDirectivePrinter.print((RequiresModuleDirective) dir, writer);
			} else if (dir instanceof OpensModuleDirective) {
				this.opensModuleDirectivePrinter.print((OpensModuleDirective) dir, writer);
			} else {
				this.exportsModuleDirectivePrinter.print((ExportsModuleDirective) dir, writer);
			}
		}
		writer.append("}\n");
	}

}
