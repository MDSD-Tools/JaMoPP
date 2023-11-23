package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.ElementReference;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.MethodCall;

public class ElementReferencePrinter {

	static void printElementReference(ElementReference element, BufferedWriter writer) throws IOException {
		if (element instanceof IdentifierReference) {
			IdentifierReferencePrinter.printIdentifierReference((IdentifierReference) element, writer);
		} else {
			MethodCallPrinter.printMethodCall((MethodCall) element, writer);
		}
	}

}
