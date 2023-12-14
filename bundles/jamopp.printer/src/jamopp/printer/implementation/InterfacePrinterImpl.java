package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.generics.TypeParametrizable;
import org.emftext.language.java.members.MemberContainer;
import org.emftext.language.java.modifiers.AnnotableAndModifiable;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

public class InterfacePrinterImpl implements Printer<Interface> {

	private final Printer<AnnotableAndModifiable> annotableAndModifiablePrinter;
	private final Printer<MemberContainer> memberContainerPrinter;
	private final Printer<TypeParametrizable> typeParametrizablePrinter;
	private final Printer<TypeReference> typeReferencePrinter;

	@Inject
	public InterfacePrinterImpl(Printer<AnnotableAndModifiable> annotableAndModifiablePrinter,
			Printer<TypeParametrizable> typeParametrizablePrinter, Printer<TypeReference> typeReferencePrinter,
			Printer<MemberContainer> memberContainerPrinter) {
		this.annotableAndModifiablePrinter = annotableAndModifiablePrinter;
		this.typeParametrizablePrinter = typeParametrizablePrinter;
		this.typeReferencePrinter = typeReferencePrinter;
		this.memberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Interface element, BufferedWriter writer) throws IOException {
		this.annotableAndModifiablePrinter.print(element, writer);
		writer.append("interface " + element.getName());
		this.typeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		if (!element.getExtends().isEmpty()) {
			writer.append("extends ");
			this.typeReferencePrinter.print(element.getExtends().get(0), writer);
			for (var index = 1; index < element.getExtends().size(); index++) {
				writer.append(", ");
				this.typeReferencePrinter.print(element.getExtends().get(index), writer);
			}
			writer.append(" ");
		}
		writer.append("{\n");
		this.memberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
