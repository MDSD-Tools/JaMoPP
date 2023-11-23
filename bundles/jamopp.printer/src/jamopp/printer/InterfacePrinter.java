package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Interface;

class InterfacePrinter {

	static void print(Interface element, BufferedWriter writer) throws IOException {
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
