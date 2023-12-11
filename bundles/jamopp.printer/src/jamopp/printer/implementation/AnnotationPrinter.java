package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;

import jamopp.printer.interfaces.Printer;

class AnnotationPrinter implements Printer<Annotation> {

	public void print(Annotation element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("@interface " + element.getName() + " {\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
