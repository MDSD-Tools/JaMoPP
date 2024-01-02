package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ElementReferencePrinterImpl implements Printer<ElementReference> {

	private final Printer<IdentifierReference> identifierReferencePrinter;
	private final Printer<MethodCall> methodCallPrinter;

	@Inject
	public ElementReferencePrinterImpl(Printer<IdentifierReference> identifierReferencePrinter,
			Printer<MethodCall> methodCallPrinter) {
		this.identifierReferencePrinter = identifierReferencePrinter;
		this.methodCallPrinter = methodCallPrinter;
	}

	@Override
	public void print(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			this.identifierReferencePrinter.print((IdentifierReference) element, writer);
		} else {
			this.methodCallPrinter.print((MethodCall) element, writer);
		}
	}

}
