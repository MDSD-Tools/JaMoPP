package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;

public class AnnotationPrinterImpl implements Printer<Annotation> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public AnnotationPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			MemberContainerPrinterInt memberContainerPrinter) {

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
