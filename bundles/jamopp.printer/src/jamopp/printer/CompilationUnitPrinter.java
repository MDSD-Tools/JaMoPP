package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;

public class CompilationUnitPrinter {

	static void printCompilationUnit(CompilationUnit element, BufferedWriter writer) throws IOException {
		for (ConcreteClassifier classifier : element.getClassifiers()) {
			ConcreteClassifierPrinter.printConcreteClassifier(classifier, writer);
		}
	}

}
