package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;

public class AnnotationPrinter {

	static void printAnnotation(Annotation element, BufferedWriter writer) throws IOException {
		AnnotableAndModifiablePrinter.printAnnotableAndModifiable(element, writer);
		writer.append("@interface " + element.getName() + " {\n");
		MemberContainerPrinter.printMemberContainer(element, writer);
		writer.append("}\n");
	}

}
