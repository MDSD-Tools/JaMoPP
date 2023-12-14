package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.AnnotableAndModifiablePrinterInt;
import jamopp.printer.interfaces.printer.ClassPrinterInt;
import jamopp.printer.interfaces.printer.ImplementorPrinterInt;
import jamopp.printer.interfaces.printer.MemberContainerPrinterInt;
import jamopp.printer.interfaces.printer.TypeParametrizablePrinterInt;
import jamopp.printer.interfaces.printer.TypeReferencePrinterInt;

public class ClassPrinter implements ClassPrinterInt {

	private final AnnotableAndModifiablePrinterInt AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinterInt TypeParametrizablePrinter;
	private final TypeReferencePrinterInt TypeReferencePrinter;
	private final ImplementorPrinterInt ImplementorPrinter;
	private final MemberContainerPrinterInt MemberContainerPrinter;

	@Inject
	public ClassPrinter(AnnotableAndModifiablePrinterInt annotableAndModifiablePrinter,
			TypeParametrizablePrinterInt typeParametrizablePrinter, TypeReferencePrinterInt typeReferencePrinter,
			ImplementorPrinterInt implementorPrinter, MemberContainerPrinterInt memberContainerPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ImplementorPrinter = implementorPrinter;
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(org.emftext.language.java.classifiers.Class element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("public class " + element.getName());
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		if (element.getExtends() != null) {
			writer.append("extends ");
			TypeReferencePrinter.print(element.getExtends(), writer);
			writer.append(" ");
		}
		ImplementorPrinter.print(element, writer);
		writer.append("{\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
