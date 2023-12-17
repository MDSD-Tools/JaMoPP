package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationPrinterImpl implements Printer<Annotation> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<MemberContainer> memberContainerPrinter;

	@Inject
	public AnnotationPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<MemberContainer> memberContainerPrinter) {

		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Annotation element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		writer.append("@interface " + element.getName() + " {\n");
		this.memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
