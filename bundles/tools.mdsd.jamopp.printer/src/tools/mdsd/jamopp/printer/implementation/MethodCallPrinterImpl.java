package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.generics.CallTypeArgumentable;
import tools.mdsd.jamopp.model.java.references.Argumentable;
import tools.mdsd.jamopp.model.java.references.MethodCall;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class MethodCallPrinterImpl implements Printer<MethodCall> {

	private final Printer<Argumentable> argumentablePrinter;
	private final Printer<CallTypeArgumentable> callTypeArgumentablePrinter;

	@Inject
	public MethodCallPrinterImpl(Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			Printer<Argumentable> argumentablePrinter) {
		this.callTypeArgumentablePrinter = callTypeArgumentablePrinter;
		this.argumentablePrinter = argumentablePrinter;
	}

	@Override
	public void print(MethodCall element, BufferedWriter writer) throws IOException {
		this.callTypeArgumentablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		this.argumentablePrinter.print(element, writer);
	}

}
