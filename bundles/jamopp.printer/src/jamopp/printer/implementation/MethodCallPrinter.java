package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class MethodCallPrinter implements Printer<MethodCall> {

	private final CallTypeArgumentablePrinter CallTypeArgumentablePrinter;
	private final ArgumentablePrinter ArgumentablePrinter;

	@Inject
	public MethodCallPrinter(jamopp.printer.implementation.CallTypeArgumentablePrinter callTypeArgumentablePrinter,
			jamopp.printer.implementation.ArgumentablePrinter argumentablePrinter) {
		super();
		CallTypeArgumentablePrinter = callTypeArgumentablePrinter;
		ArgumentablePrinter = argumentablePrinter;
	}

	@Override
	public void print(MethodCall element, BufferedWriter writer) throws IOException {
		CallTypeArgumentablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		ArgumentablePrinter.print(element, writer);
	}

}
