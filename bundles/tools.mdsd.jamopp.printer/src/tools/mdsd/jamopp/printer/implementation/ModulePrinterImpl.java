package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.modules.ExportsModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ModuleDirective;
import tools.mdsd.jamopp.model.java.modules.OpensModuleDirective;
import tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective;
import tools.mdsd.jamopp.model.java.modules.RequiresModuleDirective;
import tools.mdsd.jamopp.model.java.modules.UsesModuleDirective;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ModulePrinterImpl implements Printer<tools.mdsd.jamopp.model.java.containers.Module> {

	private final Printer<ExportsModuleDirective> exportsModuleDirectivePrinter;
	private final Printer<OpensModuleDirective> opensModuleDirectivePrinter;
	private final Printer<ProvidesModuleDirective> providesModuleDirectivePrinter;
	private final Printer<RequiresModuleDirective> requiresModuleDirectivePrinter;
	private final Printer<UsesModuleDirective> usesModuleDirectivePrinter;

	@Inject
	public ModulePrinterImpl(final Printer<UsesModuleDirective> usesModuleDirectivePrinter,
			final Printer<ProvidesModuleDirective> providesModuleDirectivePrinter,
			final Printer<RequiresModuleDirective> requiresModuleDirectivePrinter,
			final Printer<OpensModuleDirective> opensModuleDirectivePrinter,
			final Printer<ExportsModuleDirective> exportsModuleDirectivePrinter) {
		this.usesModuleDirectivePrinter = usesModuleDirectivePrinter;
		this.providesModuleDirectivePrinter = providesModuleDirectivePrinter;
		this.requiresModuleDirectivePrinter = requiresModuleDirectivePrinter;
		this.opensModuleDirectivePrinter = opensModuleDirectivePrinter;
		this.exportsModuleDirectivePrinter = exportsModuleDirectivePrinter;
	}

	@Override
	public void print(final tools.mdsd.jamopp.model.java.containers.Module element, final BufferedWriter writer)
			throws IOException {
		writer.append("module ");
		if (element.getOpen() != null) {
			writer.append("open ");
		}
		writer.append(element.getNamespacesAsString() + " {\n");
		for (final ModuleDirective dir : element.getTarget()) {
			if (dir instanceof UsesModuleDirective) {
				usesModuleDirectivePrinter.print((UsesModuleDirective) dir, writer);
			} else if (dir instanceof ProvidesModuleDirective) {
				providesModuleDirectivePrinter.print((ProvidesModuleDirective) dir, writer);
			} else if (dir instanceof RequiresModuleDirective) {
				requiresModuleDirectivePrinter.print((RequiresModuleDirective) dir, writer);
			} else if (dir instanceof OpensModuleDirective) {
				opensModuleDirectivePrinter.print((OpensModuleDirective) dir, writer);
			} else {
				exportsModuleDirectivePrinter.print((ExportsModuleDirective) dir, writer);
			}
		}
		writer.append("}\n");
	}

}
