package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;

class ClassifierReferencePrinter {

	static void print(ClassifierReference element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.print(element, writer);
		writer.append(element.getTarget().getName());
		TypeArgumentablePrinter.print(element, writer);
	}

}
