package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.types.ClassifierReference;

public class ClassifierReferencePrinter {

	static void printClassifierReference(ClassifierReference element, BufferedWriter writer) throws IOException {
		AnnotablePrinter.printAnnotable(element, writer);
		writer.append(element.getTarget().getName());
		TypeArgumentablePrinter.printTypeArgumentable(element, writer);
	}

}
