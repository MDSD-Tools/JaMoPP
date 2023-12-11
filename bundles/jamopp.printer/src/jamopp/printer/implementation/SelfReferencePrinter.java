package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.references.SelfReference;

import jamopp.printer.interfaces.Printer;

class SelfReferencePrinter implements Printer<SelfReference>{

	public void print(SelfReference element, BufferedWriter writer) throws IOException {
		SelfPrinter.print(element.getSelf(), writer);
	}

}
