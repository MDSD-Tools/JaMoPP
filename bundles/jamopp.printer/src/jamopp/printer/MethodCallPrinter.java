package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.MethodCall;

class MethodCallPrinter {

	static void print(MethodCall element, BufferedWriter writer) throws IOException {
		CallTypeArgumentablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		ArgumentablePrinter.print(element, writer);
	}

}
