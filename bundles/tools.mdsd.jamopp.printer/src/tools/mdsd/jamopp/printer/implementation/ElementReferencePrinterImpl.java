package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ElementReferencePrinterImpl implements Printer<ElementReference> {

	private final Printer<IdentifierReference> identifierReferencePrinter;
	private final Printer<MethodCall> methodCallPrinter;

	@Inject
	public ElementReferencePrinterImpl(final Printer<IdentifierReference> identifierReferencePrinter,
			final Printer<MethodCall> methodCallPrinter) {
		this.identifierReferencePrinter = identifierReferencePrinter;
		this.methodCallPrinter = methodCallPrinter;
	}

	@Override
	public void print(final ElementReference element, final BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			identifierReferencePrinter.print((IdentifierReference) element, writer);
		} else {
			methodCallPrinter.print((MethodCall) element, writer);
		}
	}

}
