package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.ClassPrinterInt;

class ClassPrinter implements Printer<org.emftext.language.java.classifiers.Class>, ClassPrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final ImplementorPrinter ImplementorPrinter;
	private final MemberContainerPrinter MemberContainerPrinter;

	@Inject
	public ClassPrinter(jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeParametrizablePrinter typeParametrizablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.ImplementorPrinter implementorPrinter,
			jamopp.printer.implementation.MemberContainerPrinter memberContainerPrinter) {
		super();
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
