package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.literals.Self;
import org.emftext.language.java.references.SelfReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class SelfReferencePrinterImpl implements Printer<SelfReference> {

	private final Printer<Self> selfPrinter;

	@Inject
	public SelfReferencePrinterImpl(Printer<Self> selfPrinter) {
		this.selfPrinter = selfPrinter;
	}

	@Override
	public void print(SelfReference element, BufferedWriter writer) throws IOException {
		this.selfPrinter.print(element.getSelf(), writer);
	}

}
