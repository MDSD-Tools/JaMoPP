package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Implementor;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class EnumerationPrinterImpl implements Printer<Enumeration> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<EnumConstant> enumConstantPrinter;
	private final Printer<Implementor> implementorPrinter;
	private final Printer<MemberContainer> memberContainerPrinter;

	@Inject
	public EnumerationPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<Implementor> implementorPrinter, Printer<EnumConstant> enumConstantPrinter,
			Printer<MemberContainer> memberContainerPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.implementorPrinter = implementorPrinter;
		this.enumConstantPrinter = enumConstantPrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Enumeration element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		writer.append("enum " + element.getName() + " ");
		this.implementorPrinter.print(element, writer);
		writer.append("{\n");
		for (EnumConstant enc : element.getConstants()) {
			this.enumConstantPrinter.print(enc, writer);
			writer.append(",\n");
		}
		if (!element.getMembers().isEmpty()) {
			writer.append(";\n\n");
			this.memberContainerPrinter.print(element, writer);
		}
		writer.append("}\n");
	}

}
