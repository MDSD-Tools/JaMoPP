package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class CompilationUnitPrinter implements Printer<CompilationUnit> {

	private final ConcreteClassifierPrinter ConcreteClassifierPrinter;

	@Inject
	public CompilationUnitPrinter(jamopp.printer.implementation.ConcreteClassifierPrinter concreteClassifierPrinter) {
		super();
		ConcreteClassifierPrinter = concreteClassifierPrinter;
	}

	@Override
	public void print(CompilationUnit element, BufferedWriter writer) throws IOException {
		for (ConcreteClassifier classifier : element.getClassifiers()) {
			ConcreteClassifierPrinter.print(classifier, writer);
		}
	}

}
