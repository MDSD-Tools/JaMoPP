package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.references.Argumentable;
import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

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
