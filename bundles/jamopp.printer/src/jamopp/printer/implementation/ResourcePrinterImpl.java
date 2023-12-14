package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.Resource;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ResourcePrinterImpl implements Printer<Resource> {

	private final Printer<ElementReference> ElementReferencePrinter;
	private final Printer<LocalVariable> LocalVariablePrinter;

	@Inject
	public ResourcePrinterImpl(Printer<LocalVariable> localVariablePrinter,
			Printer<ElementReference> elementReferencePrinter) {
		LocalVariablePrinter = localVariablePrinter;
		ElementReferencePrinter = elementReferencePrinter;
	}

	@Override
	public void print(Resource element, BufferedWriter writer) throws IOException {
		if (element instanceof LocalVariable) {
			LocalVariablePrinter.print((LocalVariable) element, writer);
		} else {
			ElementReferencePrinter.print((ElementReference) element, writer);
		}
	}

}
