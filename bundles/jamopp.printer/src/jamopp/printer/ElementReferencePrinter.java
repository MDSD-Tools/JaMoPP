package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

class ElementReferencePrinter {

	static void print(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			IdentifierReferencePrinter.printIdentifierReference((IdentifierReference) element, writer);
		} else {
			MethodCallPrinter.print((MethodCall) element, writer);
		}
	}

}
