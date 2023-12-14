package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.EnumConstant;

import com.google.inject.Inject;


import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.EnumConstantPrinterInt;
import jamopp.printer.interfaces.printer.EnumerationPrinterInt;
import jamopp.printer.interfaces.printer.ImplementorPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;

public class EnumerationPrinterImpl implements EnumerationPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final ImplementorPrinterInt ImplementorPrinter;
	private final EnumConstantPrinterInt EnumConstantPrinter;
	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public EnumerationPrinterImpl(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			ImplementorPrinterInt implementorPrinter, EnumConstantPrinterInt enumConstantPrinter,
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
