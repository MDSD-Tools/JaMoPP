package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Class;

import jamopp.printer.interfaces.Printer;

class ClassPrinter implements Printer<org.emftext.language.java.classifiers.Class>{

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final ImplementorPrinter ImplementorPrinter;
	private final MemberContainerPrinter MemberContainerPrinter;
	
	public void print(org.emftext.language.java.classifiers.Class element, BufferedWriter writer)
			throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("class " + element.getName());
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
