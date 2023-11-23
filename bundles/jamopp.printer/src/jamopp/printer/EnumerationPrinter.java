package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.members.EnumConstant;

class EnumerationPrinter {

	static void print(Enumeration element, BufferedWriter writer) throws IOException {
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
