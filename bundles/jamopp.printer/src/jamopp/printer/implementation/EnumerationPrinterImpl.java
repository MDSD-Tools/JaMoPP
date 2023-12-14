package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ImplementorPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;

public class EnumerationPrinterImpl implements Printer<Enumeration> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<EnumConstant> EnumConstantPrinter;
	private final ImplementorPrinterInt ImplementorPrinter;
	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public EnumerationPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			ImplementorPrinterInt implementorPrinter, Printer<EnumConstant> enumConstantPrinter,
			MemberContainerPrinterInt memberContainerPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		ImplementorPrinter = implementorPrinter;
		EnumConstantPrinter = enumConstantPrinter;
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Enumeration element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("enum " + element.getName() + " ");
		ImplementorPrinter.print(element, writer);
		writer.append("{\n");
		for (EnumConstant enc : element.getConstants()) {
			EnumConstantPrinter.print(enc, writer);
			writer.append(",\n");
		}
		if (!element.getMembers().isEmpty()) {
			writer.append(";\n\n");
			MemberContainerPrinter.print(element, writer);
		}
		writer.append("}\n");
	}



}
