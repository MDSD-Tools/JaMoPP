package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.CompilationUnit;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

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
