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

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<Implementor> implementorPrinter;
	private final Printer<MemberContainer> memberContainerPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public ClassPrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<Implementor> implementorPrinter, Printer<MemberContainer> memberContainerPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.implementorPrinter = implementorPrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(org.emftext.language.java.classifiers.Class element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		writer.append("class " + element.getName());
		this.typeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		if (element.getExtends() != null) {
			writer.append("extends ");
			this.typeReferencePrinter.print(element.getExtends(), writer);
			writer.append(" ");
		}
		this.implementorPrinter.print(element, writer);
		writer.append("{\n");
		this.memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
