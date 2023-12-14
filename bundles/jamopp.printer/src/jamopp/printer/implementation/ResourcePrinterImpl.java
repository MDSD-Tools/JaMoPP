package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.Resource;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.LocalVariablePrinterInt;
import jamopp.printer.interfaces.printer.ResourcePrinterInt;

public class ResourcePrinterImpl implements ResourcePrinterInt {

	private final Printer<ElementReference> ElementReferencePrinter;
	private final LocalVariablePrinterInt LocalVariablePrinter;

	@Inject
	public ResourcePrinterImpl(LocalVariablePrinterInt localVariablePrinter,
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
