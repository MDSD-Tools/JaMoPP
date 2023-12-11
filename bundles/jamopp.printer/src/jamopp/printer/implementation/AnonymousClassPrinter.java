package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.AnonymousClass;

import jamopp.printer.interfaces.Printer;

class AnonymousClassPrinter implements Printer<AnonymousClass>{

	public void print(AnonymousClass element, BufferedWriter writer) throws IOException {
		writer.append("{\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
