package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.MethodCallPrinterInt;

public class ElementReferencePrinterImpl implements Printer<ElementReference> {

	private final Printer<IdentifierReference> IdentifierReferencePrinter;
	private final MethodCallPrinterInt MethodCallPrinter;

	@Inject
	public ElementReferencePrinterImpl(Printer<IdentifierReference> identifierReferencePrinter,
			MethodCallPrinterInt methodCallPrinter) {
		IdentifierReferencePrinter = identifierReferencePrinter;
		MethodCallPrinter = methodCallPrinter;
	}

	@Override
	public void print(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			IdentifierReferencePrinter.print((IdentifierReference) element, writer);
		} else {
			MethodCallPrinter.print((MethodCall) element, writer);
		}
	}



}
