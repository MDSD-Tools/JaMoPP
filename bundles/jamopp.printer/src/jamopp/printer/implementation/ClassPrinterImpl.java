package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Implementor;
import org.emftext.language.java.generics.TypeParametrizable;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class ClassPrinterImpl implements Printer<org.emftext.language.java.classifiers.Class> {

	private final Printer<AnnotableAndModifiable> AnnotableAndModifiablePrinter;
	private final Printer<Implementor> ImplementorPrinter;
	private final Printer<MemberContainer> MemberContainerPrinter;
	private final Printer<TypeParametrizable> TypeParametrizablePrinter;
	private final Printer<TypeReference> TypeReferencePrinter;

	@Inject
	public ClassPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<Implementor> implementorPrinter, Printer<MemberContainer> memberContainerPrinter) {
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		ImplementorPrinter = implementorPrinter;
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(org.emftext.language.java.classifiers.Class element, BufferedWriter writer) throws IOException {
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
