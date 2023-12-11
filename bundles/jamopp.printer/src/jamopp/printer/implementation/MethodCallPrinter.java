package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.MethodCall;

import jamopp.printer.interfaces.Printer;

class MethodCallPrinter implements Printer<MethodCall>{

	public void print(MethodCall element, BufferedWriter writer) throws IOException {
		CallTypeArgumentablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		ArgumentablePrinter.print(element, writer);
	}

}
