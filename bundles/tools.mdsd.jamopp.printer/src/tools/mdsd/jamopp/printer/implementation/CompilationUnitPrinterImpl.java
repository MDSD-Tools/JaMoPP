package tools.mdsd.jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;

import javax.inject.Inject;

import tools.mdsd.jamopp.printer.interfaces.Printer;

public class CompilationUnitPrinterImpl implements Printer<CompilationUnit> {

	private final Printer<ConcreteClassifier> concreteClassifierPrinter;

	@Inject
	public CompilationUnitPrinterImpl(Printer<ConcreteClassifier> concreteClassifierPrinter) {
		this.concreteClassifierPrinter = concreteClassifierPrinter;
	}

	@Override
	public void print(CompilationUnit element, BufferedWriter writer) throws IOException {
		for (ConcreteClassifier classifier : element.getClassifiers()) {
			this.concreteClassifierPrinter.print(classifier, writer);
		}
	}

}
