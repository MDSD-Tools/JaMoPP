package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.model.java.variables.Resource;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ResourcePrinterImpl implements Printer<Resource> {

	private final Printer<ElementReference> elementReferencePrinter;
	private final Printer<LocalVariable> localVariablePrinter;

	@Inject
	public ResourcePrinterImpl(Printer<LocalVariable> localVariablePrinter,
			Printer<ElementReference> elementReferencePrinter) {
		this.localVariablePrinter = localVariablePrinter;
		this.elementReferencePrinter = elementReferencePrinter;
	}

	@Override
	public void print(Resource element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			this.localVariablePrinter.print((LocalVariable) element, writer);
		} else {
			this.elementReferencePrinter.print((ElementReference) element, writer);
		}
	}

}
