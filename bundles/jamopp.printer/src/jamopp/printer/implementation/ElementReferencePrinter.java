package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

import jamopp.printer.interfaces.Printer;

class ElementReferencePrinter implements Printer<ElementReference>{

	private final IdentifierReferencePrinter IdentifierReferencePrinter;
	private final MethodCallPrinter MethodCallPrinter;
	
	public void print(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			IdentifierReferencePrinter.printIdentifierReference((IdentifierReference) element, writer);
		} else {
			MethodCallPrinter.print((MethodCall) element, writer);
		}
	}

}
