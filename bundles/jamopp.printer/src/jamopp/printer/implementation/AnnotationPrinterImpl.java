package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class AnnotationPrinterImpl implements Printer<Annotation> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<MemberContainer> MemberContainerPrinter;

	@Inject
	public AnnotationPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<MemberContainer> memberContainerPrinter) {

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
