package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class AnnotationPrinterImpl implements Printer<Annotation> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<MemberContainer> memberContainerPrinter;

	@Inject
	public AnnotationPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<MemberContainer> memberContainerPrinter) {

		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(final Annotation element, final BufferedWriter writer) throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		writer.append("@interface " + element.getName() + " {\n");
		memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
