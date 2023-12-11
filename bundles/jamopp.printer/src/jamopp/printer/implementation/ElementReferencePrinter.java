package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class ElementReferencePrinter implements Printer<ElementReference> {

	private final IdentifierReferencePrinter IdentifierReferencePrinter;
	private final MethodCallPrinter MethodCallPrinter;

	@Inject
	public ElementReferencePrinter(jamopp.printer.implementation.IdentifierReferencePrinter identifierReferencePrinter,
			jamopp.printer.implementation.MethodCallPrinter methodCallPrinter) {
		super();
		IdentifierReferencePrinter = identifierReferencePrinter;
		MethodCallPrinter = methodCallPrinter;
	}

	public void print(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			IdentifierReferencePrinter.printIdentifierReference((IdentifierReference) element, writer);
		} else {
			MethodCallPrinter.print((MethodCall) element, writer);
		}
	}

}
