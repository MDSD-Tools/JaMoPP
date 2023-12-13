package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotationPrinterInt;

class AnnotationPrinter implements AnnotationPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final MemberContainerPrinter MemberContainerPrinter;

	@Inject
	public AnnotationPrinter(jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.MemberContainerPrinter memberContainerPrinter) {
		super();
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Annotation element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("@interface " + element.getName() + " {\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
