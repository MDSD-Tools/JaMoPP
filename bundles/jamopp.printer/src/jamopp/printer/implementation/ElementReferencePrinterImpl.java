package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
