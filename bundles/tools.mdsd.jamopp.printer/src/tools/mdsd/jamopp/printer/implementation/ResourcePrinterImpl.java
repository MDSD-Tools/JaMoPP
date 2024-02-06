package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.Resource;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ResourcePrinterImpl implements Printer<Resource> {

	private final Printer<ElementReference> elementReferencePrinter;
	private final Printer<LocalVariable> localVariablePrinter;

	@Inject
	public ResourcePrinterImpl(final Printer<LocalVariable> localVariablePrinter,
			final Printer<ElementReference> elementReferencePrinter) {
		this.localVariablePrinter = localVariablePrinter;
		this.elementReferencePrinter = elementReferencePrinter;
	}

	@Override
	public void print(final Resource element, final BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			localVariablePrinter.print((LocalVariable) element, writer);
		} else {
			elementReferencePrinter.print((ElementReference) element, writer);
		}
	}

}
