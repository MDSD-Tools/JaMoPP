package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.references.Argumentable;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MethodCallPrinterImpl implements Printer<MethodCall> {

	private final Printer<Argumentable> argumentablePrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;

	@Inject
	public MethodCallPrinterImpl(final Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			final Printer<Argumentable> argumentablePrinter) {
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.argumentablePrinter = argumentablePrinter;
	}

	@Override
	public void print(final MethodCall element, final BufferedWriter writer) throws IOException {
		callTypeArgumentablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		argumentablePrinter.print(element, writer);
	}

}
