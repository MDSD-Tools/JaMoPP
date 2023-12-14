package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.AnnotationPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;

public class AnnotationPrinter implements AnnotationPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public AnnotationPrinter(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
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
