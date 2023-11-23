package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Class;

public class ClassPrinter {

	static void printClass(org.emftext.language.java.classifiers.Class element, BufferedWriter writer)
			throws IOException {
		AnnotableAndModifiablePrinter.print(element, writer);
		writer.append("class " + element.getName());
		TypeParametrizablePrinter.printTypeParametrizable(element, writer);
		writer.append(" ");
		if (element.getExtends() != null) {
			writer.append("extends ");
			TypeReferencePrinter.printTypeReference(element.getExtends(), writer);
			writer.append(" ");
		}
		ImplementorPrinter.printImplementor(element, writer);
		writer.append("{\n");
		MemberContainerPrinter.printMemberContainer(element, writer);
		writer.append("}\n");
	}

}
