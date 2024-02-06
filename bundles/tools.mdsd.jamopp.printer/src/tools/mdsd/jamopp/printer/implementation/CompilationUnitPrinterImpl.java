package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CompilationUnitPrinterImpl implements Printer<CompilationUnit> {

	private final Printer<ConcreteClassifier> concreteClassifierPrinter;

	@Inject
	public CompilationUnitPrinterImpl(final Printer<ConcreteClassifier> concreteClassifierPrinter) {
		this.concreteClassifierPrinter = concreteClassifierPrinter;
	}

	@Override
	public void print(final CompilationUnit element, final BufferedWriter writer) throws IOException {
		for (final ConcreteClassifier classifier : element.getClassifiers()) {
			concreteClassifierPrinter.print(classifier, writer);
		}
	}

}
