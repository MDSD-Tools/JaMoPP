package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.SelfReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class SelfReferencePrinter implements Printer<SelfReference> {

	private final SelfPrinter SelfPrinter;

	@Inject
	public SelfReferencePrinter(jamopp.printer.implementation.SelfPrinter selfPrinter) {
		super();
		SelfPrinter = selfPrinter;
	}

	public void print(SelfReference element, BufferedWriter writer) throws IOException {
		SelfPrinter.print(element.getSelf(), writer);
	}

}
