package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Implementor;
import tools.mdsd.jamopp.model.java.generics.TypeParametrizable;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class ClassPrinterImpl implements Printer<tools.mdsd.jamopp.model.java.classifiers.Class> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<Implementor> implementorPrinter;
	private final Printer<MemberContainer> memberContainerPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ClassPrinterImpl(final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			final Printer<TypeParametrizable> typeParametrizablePrinter,
			final Printer<TypeReference> typeReferencePrinter, final Printer<Implementor> implementorPrinter,
			final Printer<MemberContainer> memberContainerPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.implementorPrinter = implementorPrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(final tools.mdsd.jamopp.model.java.classifiers.Class element, final BufferedWriter writer)
			throws IOException {
		annotableAndModifiablePrinter.print(element, writer);
		writer.append("class " + element.getName());
		typeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		if (element.getExtends() != null) {
			writer.append("extends ");
			typeReferencePrinter.print(element.getExtends(), writer);
			writer.append(" ");
		}
		implementorPrinter.print(element, writer);
		writer.append("{\n");
		memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
