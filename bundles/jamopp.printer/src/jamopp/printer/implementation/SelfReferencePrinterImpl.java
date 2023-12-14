package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.SelfReference;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.SelfPrinterInt;
import jamopp.printer.interfaces.printer.SelfReferencePrinterInt;

public class SelfReferencePrinterImpl implements SelfReferencePrinterInt {

	private final SelfPrinterInt SelfPrinter;

	@Inject
	public SelfReferencePrinterImpl(SelfPrinterInt selfPrinter) {
		SelfPrinter = selfPrinter;
	}

	@Override
	public void print(SelfReference element, BufferedWriter writer) throws IOException {
		SelfPrinter.print(element.getSelf(), writer);
	}

}
