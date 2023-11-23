package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;

class CompilationUnitPrinter {

	static void print(CompilationUnit element, BufferedWriter writer) throws IOException {
		for (ConcreteClassifier classifier : element.getClassifiers()) {
			ConcreteClassifierPrinter.print(classifier, writer);
		}
	}

}
