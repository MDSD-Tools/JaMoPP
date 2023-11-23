package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Class;

class ClassPrinter {

	static void print(org.emftext.language.java.classifiers.Class element, BufferedWriter writer)
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
