package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Enumeration;
import org.emftext.language.java.classifiers.Interface;

class ConcreteClassifierPrinter {

	static void print(ConcreteClassifier element, BufferedWriter writer) throws IOException {
		if (element instanceof org.emftext.language.java.classifiers.Class) {
			ClassPrinter.print((org.emftext.language.java.classifiers.Class) element, writer);
		} else if (element instanceof Interface) {
			InterfacePrinter.print((Interface) element, writer);
		} else if (element instanceof Enumeration) {
			EnumerationPrinter.print((Enumeration) element, writer);
		} else {
			AnnotationPrinter.print((Annotation) element, writer);
		}
	}

}
