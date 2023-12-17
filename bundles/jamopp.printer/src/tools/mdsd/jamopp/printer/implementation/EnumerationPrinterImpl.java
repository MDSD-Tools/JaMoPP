package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Implementor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

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
