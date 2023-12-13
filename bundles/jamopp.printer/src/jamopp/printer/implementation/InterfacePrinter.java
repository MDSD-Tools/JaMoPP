package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Interface;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;
import jamopp.printer.interfaces.printer.InterfacePrinterInt;

public class InterfacePrinter implements InterfacePrinterInt {

	private final AnnotableAndModifiablePrinter AnnotableAndModifiablePrinter;
	private final TypeParametrizablePrinter TypeParametrizablePrinter;
	private final TypeReferencePrinter TypeReferencePrinter;
	private final MemberContainerPrinter MemberContainerPrinter;

	@Inject
	public InterfacePrinter(jamopp.printer.implementation.AnnotableAndModifiablePrinter annotableAndModifiablePrinter,
			jamopp.printer.implementation.TypeParametrizablePrinter typeParametrizablePrinter,
			jamopp.printer.implementation.TypeReferencePrinter typeReferencePrinter,
			jamopp.printer.implementation.MemberContainerPrinter memberContainerPrinter) {
		super();
		AnnotableAndModifiablePrinter = annotableAndModifiablePrinter;
		TypeParametrizablePrinter = typeParametrizablePrinter;
		TypeReferencePrinter = typeReferencePrinter;
		MemberContainerPrinter = memberContainerPrinter;
	}

	@Override
	public void print(Interface element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("interface " + element.getName());
		TypeParametrizablePrinter.print(element, writer);
		writer.append(" ");
		if (!element.getExtends().isEmpty()) {
			writer.append("extends ");
			TypeReferencePrinter.print(element.getExtends().get(0), writer);
			for (int index = 1; index < element.getExtends().size(); index++) {
				writer.append(", ");
				TypeReferencePrinter.print(element.getExtends().get(index), writer);
			}
			writer.append(" ");
		}
		writer.append("{\n");
		MemberContainerPrinter.print(element, writer);
		writer.append("}\n");
	}

}
