package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.generics.CallTypeArgumentable;
import org.emftext.language.java.references.Argumentable;
import org.emftext.language.java.references.MethodCall;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class MethodCallPrinterImpl implements Printer<MethodCall> {

	private final Printer<Argumentable> ArgumentablePrinter;
	private final Printer<CallTypeArgumentable> CallTypeArgumentablePrinter;

	@Inject
	public MethodCallPrinterImpl(Printer<CallTypeArgumentable> callTypeArgumentablePrinter,
			Printer<Argumentable> argumentablePrinter) {
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
