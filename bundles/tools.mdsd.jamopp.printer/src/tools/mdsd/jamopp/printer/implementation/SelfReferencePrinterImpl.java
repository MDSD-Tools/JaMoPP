package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.literals.Self;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class SelfReferencePrinterImpl implements Printer<SelfReference> {

	private final Printer<Self> selfPrinter;

	@Inject
	public SelfReferencePrinterImpl(final Printer<Self> selfPrinter) {
		this.selfPrinter = selfPrinter;
	}

	@Override
	public void print(final SelfReference element, final BufferedWriter writer) throws IOException {
		selfPrinter.print(element.getSelf(), writer);
	}

}
