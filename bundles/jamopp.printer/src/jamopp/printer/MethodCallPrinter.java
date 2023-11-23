package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.MethodCall;

public class MethodCallPrinter {

	static void printMethodCall(MethodCall element, BufferedWriter writer) throws IOException {
		CallTypeArgumentablePrinter.printCallTypeArgumentable(element, writer);
		writer.append(element.getTarget().getName());
		ArgumentablePrinter.printArgumentable(element, writer);
	}

}
